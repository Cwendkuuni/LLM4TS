
import org.junit.Test;
import static org.junit.Assert.*;

public class OnlineSchemaChangeParametersTest {

  @Test
  public void testIsContinueOnError() {
    OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
    parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
    assertTrue(parameters.isContinueOnError());
  }

  @Test
  public void testGenerateSubTaskParameters() {
    OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
    parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
    parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column INT;");
    parameters.setDelimiter(";");
    
    ConnectionConfig connectionConfig = new ConnectionConfig();
    connectionConfig.setDialectType(DialectType.MYSQL);
    
    List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
    
    assertNotNull(subTaskParameters);
    assertEquals(1, subTaskParameters.size());
  }
}
