import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class  OnlineSchemaChangeParametersTest {

    OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() throws Exception {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError(){
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());
        
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(null);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }
  
    @Test
    public void testGenerateSubTaskParameters() {
        String delimiter = ";";
        String sqlContent= "select * from table1; select * from table2;";

        onlineSchemaChangeParameters.setDelimiter(delimiter);
        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        ConnectionConfig connectionConfig= mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        try {
            onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "testSchema");
        } catch (Exception e) {
            assertTrue(e instanceof UnexpectedException);
            e.printStackTrace();
        }
    }
}