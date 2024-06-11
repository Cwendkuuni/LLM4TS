import org.junit.Assert;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {
  
  @Test
  public void testIsContinueOnError(){
    OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
    parameters.setErrorStrategy(TaskErrorStrategy.SKIP);
    Assert.assertFalse(parameters.isContinueOnError());

    parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
    Assert.assertTrue(parameters.isContinueOnError());
  }
  
  @Test(expected = NullPointerException.class)
  public void testIsContinueOnErrorWithNull(){
    OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
    parameters.setErrorStrategy(null);
    parameters.isContinueOnError();
  }

  // assuming that ConnectionConfig and sqlContent are already tested in their own test cases

  @Test(expected = UnexpectedException.class)
  public void testGenerateSubTaskParametersWithNullConnectionConfig(){
    OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
    parameters.setSqlContent("SELECT * FROM table;");
    parameters.generateSubTaskParameters(null, "schema");
  }

  @Test(expected = UnexpectedException.class)
  public void testGenerateSubTaskParametersWithNullSchema(){
    OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
    parameters.setSqlContent("SELECT * FROM table;");
    ConnectionConfig config = new ConnectionConfig();
    parameters.generateSubTaskParameters(config, null);
  }
  
  @Test(expected = UnexpectedException.class)
  public void testGenerateSubTaskParametersWithInvalidSqlContent(){
    OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
    parameters.setSqlContent("INVALID_SQL_STATEMENT");
    ConnectionConfig config = new ConnectionConfig();
    parameters.generateSubTaskParameters(config, "schema");
  }
}