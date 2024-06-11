import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());
        
        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Test case for typical scenario
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        params.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY)");
        params.setErrorStrategy(TaskErrorStrategy.STOP);
        params.setDelimiter(";");
        params.setLockUsers(new ArrayList<>());
        params.setSwapTableType(SwapTableType.SWAP_RENAME);
        
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        String schema = "test_schema";
        
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(connectionConfig, schema);
        
        assertNotNull(subTaskParams);
        assertEquals(1, subTaskParams.size());
        
        // Test case for edge cases
        // Add more edge cases here
    }
}