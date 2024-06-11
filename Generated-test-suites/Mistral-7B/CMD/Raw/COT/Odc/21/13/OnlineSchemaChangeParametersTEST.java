import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(OnlineSchemaChangeSqlType.CREATE, OnlineSchemaChangeSqlType.CREATE);
        assertNull(parameters.getSqlContent());
        assertFalse(parameters.isContinueOnError());
        assertNull(parameters.getLockTableTimeOutSeconds());
        assertNull(parameters.getSwapTableNameRetryTimes());
        assertNull(parameters.getOriginTableCleanStrategy());
        assertNull(parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertNull(parameters.getSwapTableType());

        OnlineSchemaChangeParameters parameters1 = new OnlineSchemaChangeParameters();
        parameters1.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters1.setsSqlContent("test sql content");
        parameters1.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters1.setLockTableTimeOutSeconds(10);
        parameters1.setSwapTableNameRetryTimes(3);
        parameters1.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        parameters1.setDelimiter(";");
        parameters1.setLockUsers(new ArrayList<>(Arrays.asList("test1", "test2", "test3")));
        parameters1.setSwapTableType(SwapTableType.TEMPORARY);

        assertEquals(OnlineSchemaChangeSqlType.ALTER, OnlineSchemaChangeSqlType.ALTER);
        assertEquals("test sql content", parameters1.getSqlContent());
        assertTrue(parameters1.isContinueOnError());
        assertEquals(10, parameters1.getLockTableTimeOutSeconds());
        assertEquals(3, parameters1.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.DROP, parameters1.getOriginTableCleanStrategy());
        assertEquals(";", parameters1.getDelimiter());
        assertNotNull(parameters1.getLockUsers());
        assertEquals(3, parameters1.getLockUsers().size());
        assertEquals(SwapTableType.TEMPORARY, parameters1.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertFalse(parameters.isContinueOnError());

        OnlineSchemaChangeParameters parameters1 = new OnlineSchemaChangeParameters();
        parameters1.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters1.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParametersNullConnectionConfig() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertThrows(NullPointerException.class, () -> parameters.generateSubTaskParameters(null, "testSchema"));
    }

    @Test
    public void testGenerateSubTaskParametersNullSchema() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        assertThrows(NullPointerException.class, () -> parameters.generateSubTaskParameters(connectionConfig, null));
    }

    @Test
    public void testGenerateSubTaskParametersEmptySqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setsSqlContent("");

        try {
            parameters.generateSubTaskParameters(connectionConfig, schema);
            fail("Expected UnexpectedException");
        } catch (UnexpectedException e) {
            // Expected
        }
    }

    @Test
    public void testGenerateSubTaskParametersValidParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setsSqlContent("test sql content; test sql content2");

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertNotNull(taskParameters);
        assertEquals(2, taskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameters1 = taskParameters.get(0);
        OnlineSchemaChangeScheduleTaskParameters taskParameters2 = taskParameters.get(1);

        assertEquals("testSchema", taskParameters1.getDatabaseName());
        assertEquals("testTable1", taskParameters1.getOriginTableName());
        assertEquals("testSql1", taskParameters1.getSqlsToBeExecuted().get(0));

        assertEquals("testSchema", taskParameters2.getDatabaseName());
        assertEquals("testTable2", taskParameters2.getOriginTableName());
        assertEquals("testSql2", taskParameters2.getSqlsToBeExecuted().get(0));
    }

    @Test
    public void testGenerateSubTaskParametersWithAlterStatement() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setsSqlContent("alter table testTable1 add column newColumn int; alter table testTable1 rename to testTable2");

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertNotNull(taskParameters);
        assertEquals(2, taskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameters1 = taskParameters.get(0);
        OnlineSchemaChangeScheduleTaskParameters taskParameters2 = taskParameters.get(1);

        assertEquals("testSchema", taskParameters1.getDatabaseName());
        assertEquals("testTable1", taskParameters1.getOriginTableName());
        assertEquals("alter table testTable1 add column newColumn int", taskParameters1.getSqlsToBeExecuted().get(0));
        assertEquals("alter table testTable1 rename to testTable3", taskParameters1.getSqlsToBeExecuted().get(1));

        assertEquals("testSchema", taskParameters2.getDatabaseName());
        assertEquals("testTable2", taskParameters2.getOriginTableName());
        assertEquals("alter table testTable2 rename to testTable3", taskParameters2.getSqlsToBeExecuted().get(0));
    }

    @Test
    public void testGenerateSubTaskParametersWithEmptySqlsToBeExecuted() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setsSqlContent("alter table testTable1 add column newColumn int;");

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertNotNull(taskParameters);
        assertEquals(1, taskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameters1 = taskParameters.get(0);
        assertEquals("testSchema", taskParameters1.getDatabaseName());
        assertEquals("testTable1", taskParameters1.getOriginTableName());
        assertNotNull(taskParameters1.getSqlsToBeExecuted());
        assertEquals(0, taskParameters1.getSqlsToBeExecuted().size());
    }

    @Test
    public void testGenerateSubTaskParametersWithNullSqlsToBeExecuted() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setsSqlContent("alter table testTable1 add column newColumn int;");

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertNotNull(taskParameters);
        assertEquals(1, taskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameters1 = taskParameters.get(0);
        assertEquals("testSchema", taskParameters1.getDatabaseName());
        assertEquals("testTable1", taskParameters1.getOriginTableName());
        assertNull(taskParameters1.getSqlsToBeExecuted());
    }

    @Test
    public void testGenerateSubTaskParametersWithInvalidSqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setsSqlContent("test sql content; test sql content2;");

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertNotNull(taskParameters);
        assertEquals(2, taskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameters1 = taskParameters.get(0);
        OnlineSchemaChangeScheduleTaskParameters taskParameters2 = taskParameters.get(1);

        assertEquals("testSchema", taskParameters1.getDatabaseName());
        assertEquals("testTable1", taskParameters1.getOriginTableName());
        assertEquals("test sql content", taskParameters1.getSqlsToBeExecuted().get(0));
        assertEquals("test sql content2", taskParameters1.getSqlsToBeExecuted().get(1));

        assertEquals("testSchema", taskParameters2.getDatabaseName());
        assertEquals("testTable2", taskParameters2.getOriginTableName());
        assertEquals("test sql content3", taskParameters2.getSqlsToBeExecuted().get(0));
        assertEquals("test sql content4", taskParameters2.getSqlsToBeExecuted().get(1));
    }

    @Test
    public void testGenerateSubTaskParametersWithEmptyLockUsers() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setsSqlContent("alter table testTable1 add column newColumn int;");
        parameters.setLockUsers(Collections.emptyList());

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertNotNull(taskParameters);
        assertEquals(0, taskParameters.size());
    }

}