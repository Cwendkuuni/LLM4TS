package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        connectionConfig = mock(ConnectionConfig.class);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testGettersAndSetters() {
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN column1 INT;");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.SWAP);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockUsers(Arrays.asList("user1", "user2"));

        assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
        assertEquals("ALTER TABLE table1 ADD COLUMN column1 INT;", onlineSchemaChangeParameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
        assertEquals(30, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.SWAP, onlineSchemaChangeParameters.getSwapTableType());
        assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
        assertEquals(Arrays.asList("user1", "user2"), onlineSchemaChangeParameters.getLockUsers());

        onlineSchemaChangeParameters.setSqlType(null);
        onlineSchemaChangeParameters.setSqlContent(null);
        onlineSchemaChangeParameters.setErrorStrategy(null);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(null);
        onlineSchemaChangeParameters.setSwapTableType(null);
        onlineSchemaChangeParameters.setDelimiter(null);
        onlineSchemaChangeParameters.setLockUsers(null);

        assertNull(onlineSchemaChangeParameters.getSqlType());
        assertNull(onlineSchemaChangeParameters.getSqlContent());
        assertNull(onlineSchemaChangeParameters.getErrorStrategy());
        assertNull(onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
        assertNull(onlineSchemaChangeParameters.getSwapTableType());
        assertNull(onlineSchemaChangeParameters.getDelimiter());
        assertNull(onlineSchemaChangeParameters.getLockUsers());
    }

    @Test
    public void testIsContinueOnError() {
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        when(connectionConfig.getDialectType()).thenReturn("mysql");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE table1 ADD COLUMN column1 INT;");

        List<String> sqls = Arrays.asList("ALTER TABLE table1 ADD COLUMN column1 INT;",
                "ALTER TABLE table1 MODIFY column1 INT;");

        when(SubTaskParameterFactory.newInstance()).thenReturn(mock(SubTaskParameterFactory.class));

        try {
            OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
            parameters.setSqlContent(onlineSchemaChangeParameters.getSqlContent());
            parameters.setSqlType(onlineSchemaChangeParameters.getSqlType());
            List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, null);

            assertEquals(2, result.size());

            OnlineSchemaChangeScheduleTaskParameters task1 = result.get(0);
            OnlineSchemaChangeScheduleTaskParameters task2 = result.get(1);

            assertEquals("table1", task1.getOriginTableName());
            assertEquals("table1", task2.getOriginTableName());
            assertEquals("mysql", task1.getDatabaseName());
            assertEquals("mysql", task2.getDatabaseName());
            assertEquals(OnlineSchemaChangeSqlType.ALTER, task1.getSqlType());
            assertEquals(OnlineSchemaChangeSqlType.ALTER, task2.getSqlType());
            assertEquals(Arrays.asList("ALTER TABLE table1 ADD COLUMN column1 INT;"), task1.getSqlsToBeExecuted());
            assertEquals(Arrays.asList("ALTER TABLE table1 MODIFY column1 INT;"), task2.getSqlsToBeExecuted());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(SubTaskParameterFactory.newInstance()).thenThrow(new RuntimeException());

        try {
            onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, null);
            assert false; // this line should never be reached
        } catch (UnexpectedException e) {
            // expected
        }
    }

}