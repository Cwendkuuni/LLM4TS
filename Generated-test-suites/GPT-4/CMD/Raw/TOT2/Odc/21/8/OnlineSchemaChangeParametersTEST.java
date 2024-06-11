import static org.junit.Assert.*;
import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.model.*;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters oscp = new OnlineSchemaChangeParameters();
        oscp.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(oscp.isContinueOnError());
        
        oscp.setErrorStrategy(TaskErrorStrategy.UNKNOWN);
        assertFalse(oscp.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters(){
        OnlineSchemaChangeParameters oscp = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.ORACLE);  //assuming one dialect type
        oscp.setSqlContent("SELECT * FROM table_name");
        oscp.setDelimiter(";");
        oscp.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        List<OnlineSchemaChangeScheduleTaskParameters> result=subTaskParameters = oscp.generateSubTaskParameters(connectionConfig, "schema");
        assertTrue(!result.isEmpty());  
    }
}