package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

/**
 * Test class for OnlineSchemaChangeParameters
 *
 * @author yaobin
 * @since 4.2.0
 */
public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;

    @Before
    public void init() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
    }

    /**
     * Test constructor with valid arguments
     */
    @Test
    public void testValidConstructor() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test ALTER COLUMN id INT NULL COMMENT 'new comment'");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.SWAP_TO_OLD);
        // other setters can be added here if needed
        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
        assertEquals(10, parameters.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.SWAP_TO_OLD, parameters.getSwapTableType());
    }

    /**
     * Test constructor with null sqlContent
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullSqlContent() {
        new OnlineSchemaChangeParameters().setSqlType(OnlineSchemaChangeSqlType.ALTER);
        new OnlineSchemaChangeParameters().setSqlContent(null);
    }

    /**
     * Test method generateSubTaskParameters with valid arguments
     */
    @Test
    public void testGenerateSubTaskParametersValid() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test ALTER COLUMN id INT NULL COMMENT 'new comment'");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.SWAP_TO_OLD);

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "test");
        assertEquals(1, result.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = result.get(0);
        assertEquals("test", taskParameter.getDatabaseName());
        assertEquals("test", taskParameter.getOriginTableName());
        assertEquals("ALTER TABLE test ALTER COLUMN id INT NULL COMMENT 'new comment'", taskParameter.getSqlContent());
        assertEquals(Arrays.asList("ALTER TABLE test ALTER COLUMN id INT NULL COMMENT 'new comment'"), taskParameter.getSqlsToBeExecuted());
    }

    /**
     * Test method generateSubTaskParameters with invalid sqlContent
     */
    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersInvalid() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test ALTER COLUMN id INT NULL COMMENT 'new comment'; DROP TABLE test");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.SWAP_TO_OLD);

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "test");
        assertEquals(1, result.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = result.get(0);
        assertEquals("test", taskParameter.getDatabaseName());
        assertEquals("test", taskParameter.getOriginTableName());
        assertEquals("ALTER TABLE test ALTER COLUMN id INT NULL COMMENT 'new comment'", taskParameter.getSqlContent());
        // check that only the first SQL is executed, not the second one
        assertEquals(Collections.singletonList("ALTER TABLE test ALTER COLUMN id INT NULL COMMENT 'new comment'"), taskParameter.getSqlsToBeExecuted());
    }

}