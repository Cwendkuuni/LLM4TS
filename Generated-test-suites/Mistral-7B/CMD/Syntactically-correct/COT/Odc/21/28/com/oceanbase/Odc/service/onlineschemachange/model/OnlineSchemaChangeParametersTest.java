import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testInstance() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNull(parameters.getSqlType());
        assertNull(parameters.getSqlContent());
        assertNull(parameters.getErrorStrategy());
        assertNull(parameters.getLockTableTimeOutSeconds());
        assertNull(parameters.getSwapTableNameRetryTimes());
        assertNull(parameters.getOriginTableCleanStrategy());
        assertNull(parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertNull(parameters.getSwapTableType());
    }

    @Test
    public void testSettersAndGetters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "testSqlContent";
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        Integer lockTableTimeOutSeconds = 5;
        Integer swapTableNameRetryTimes = 3;
        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.DROP;
        String delimiter = ";";
        List<String> lockUsers = Arrays.asList("user1", "user2");
        SwapTableType swapTableType = SwapTableType.SAME_TABLESPACE;
        parameters.setSqlType(sqlType);
        parameters.setSqlContent(sqlContent);
        parameters.setErrorStrategy(errorStrategy);
        parameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
        parameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
        parameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        parameters.setDelimiter(delimiter);
        parameters.setLockUsers(lockUsers);
        parameters.setSwapTableType(swapTableType);
        assertSame(sqlType, parameters.getSqlType());
        assertSame(sqlContent, parameters.getSqlContent());
        assertSame(errorStrategy, parameters.getErrorStrategy());
        assertSame(lockTableTimeOutSeconds, parameters.getLockTableTimeOutSeconds());
        assertSame(swapTableNameRetryTimes, parameters.getSwapTableNameRetryTimes());
        assertSame(originTableCleanStrategy, parameters.getOriginTableCleanStrategy());
        assertSame(delimiter, parameters.getDelimiter());
        assertSame(lockUsers, parameters.getLockUsers());
        assertSame(swapTableType, parameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters1 = new OnlineSchemaChangeParameters();
        assertFalse(parameters1.isContinueOnError());
        OnlineSchemaChangeParameters parameters2 = new OnlineSchemaChangeParameters();
        parameters2.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters2.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters_normal() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        String sqlContent = "CREATE TABLE testTable (id INT PRIMARY KEY) ENGINE=OBSERVER";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, result.size());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_exception() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        String sqlContent = "CREATE TABLE testTable (id INT PRIMARY KEY) ENGINE=OBSERVER";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, schema);
        assertThrows(UnexpectedException.class, () -> parameters.generateSubTaskParameters(connectionConfig, schema));
    }

    @Test
    public void testGenerateSubTaskParameters_emptySqls() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        String sqlContent = "";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(0, result.size());
    }

    @Test
    public void testGenerateSubTaskParameters_multipleQueries() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        String sqlContent = "CREATE TABLE testTable1 (id INT PRIMARY KEY) ENGINE=OBSERVER;\n"
                + "CREATE TABLE testTable2 (id INT PRIMARY KEY) ENGINE=OBSERVER;";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(2, result.size());
    }

    @Test
    public void testGenerateSubTaskParameters_singleQueryWithSwapTable() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        String sqlContent = "CREATE TABLE testTable (id INT PRIMARY KEY) ENGINE=OBSERVER;\n"
                + "ALTER TABLE testTable RENAME TO testTable_old;\n"
                + "CREATE TABLE testTable ENGINE=OBSERVER;\n"
                + "ALTER TABLE testTable ADD COLUMN col INT;\n";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(3, result.size());
        OnlineSchemaChangeScheduleTaskParameters firstTask = result.get(0);
        OnlineSchemaChangeScheduleTaskParameters secondTask = result.get(1);
        OnlineSchemaChangeScheduleTaskParameters thirdTask = result.get(2);
        assertEquals("testTable", firstTask.getOriginTableName());
        assertEquals("testSchema_testTable_old", firstTask.getNewTableName());
        assertEquals("CREATE TABLE testTable ENGINE=OBSERVER;\n", secondTask.getSqlContent());
        assertEquals("testTable", secondTask.getOriginTableName());
        assertEquals("testSchema.testTable", secondTask.getNewTableName());
        assertEquals("ALTER TABLE testTable ADD COLUMN col INT;\n", thirdTask.getSqlContent());
        assertEquals("testTable", thirdTask.getOriginTableName());
        assertEquals("testSchema.testTable", thirdTask.getNewTableName());
    }

    @Test
    public void testGenerateSubTaskParameters_singleQueryWithoutSwapTable() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        String sqlContent = "CREATE TABLE testTable (id INT PRIMARY KEY) ENGINE=OBSERVER;\n"
                + "ALTER TABLE testTable ADD COLUMN col INT;\n";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, result.size());
        OnlineSchemaChangeScheduleTaskParameters task = result.get(0);
        assertEquals("testTable", task.getOriginTableName());
        assertEquals("testSchema.testTable", task.getNewTableName());
        assertEquals("ALTER TABLE testTable ADD COLUMN col INT;\n", task.getSqlContent());
    }

    @Test
    public void testGenerateSubTaskParameters_emptySqlsAndErrorStrategy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        String sqlContent = "";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(0, result.size());
    }

    @Test
    public void testGenerateSubTaskParameters_sqlsWithErrorStrategy() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        String sqlContent = "CREATE TABLE testTable1 (id INT PRIMARY KEY) ENGINE=OBSERVER;\n"
                + "CREATE TABLE testTable2 (id INT PRIMARY KEY) ENGINE=OBSERVER;\n"
                + "DROP TABLE testTable3;"; // expected to fail
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(2, result.size());
    }

    @Test
    public void testGenerateSubTaskParameters_sqlsWithErrorStrategyAndContinue() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        String sqlContent = "CREATE TABLE testTable1 (id INT PRIMARY KEY) ENGINE=OBSERVER;\n"
                + "CREATE TABLE testTable2 (id INT PRIMARY KEY) ENGINE=OBSERVER;\n"
                + "DROP TABLE testTable3;"; // expected to fail
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(3, result.size());
    }

    @Test
    public void testGenerateSubTaskParameters_sqlsWithErrorStrategyAndDrop() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        String sqlContent = "CREATE TABLE testTable1 (id INT PRIMARY KEY) ENGINE=OBSERVER;\n"
                + "CREATE TABLE testTable2 (id INT PRIMARY KEY) ENGINE=OBSERVER;\n"
                + "DROP TABLE testTable3;"; // expected to fail
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setErrorStrategy(TaskErrorStrategy.DROP);
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(2, result.size());
    }

    enum OriginTableCleanStrategy {
        DROP,
        TRUNCATE
    }

}