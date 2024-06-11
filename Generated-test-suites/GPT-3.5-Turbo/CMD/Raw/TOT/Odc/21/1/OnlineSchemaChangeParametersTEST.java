import static org.junit.Assert.*;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

  @Test
  public void testIsContinueOnError() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
    assertTrue(params.isContinueOnError());

    params.setErrorStrategy(TaskErrorStrategy.ABORT);
    assertFalse(params.isContinueOnError());
  }

  @Test
  public void testGenerateSubTaskParameters() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
    params.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column INT;");
    params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
    params.setDelimiter(";");
    params.setSwapTableType(SwapTableType.SYNC);

    ConnectionConfig connectionConfig = new ConnectionConfig();
    connectionConfig.setDialectType(DialectType.MYSQL);

    List<OnlineSchemaChangeScheduleTaskParameters> subTasks = params.generateSubTaskParameters(connectionConfig, "test_schema");

    assertEquals(1, subTasks.size());
    OnlineSchemaChangeScheduleTaskParameters subTaskParams = subTasks.get(0);
    assertEquals("test_schema", subTaskParams.getDatabaseName());
    assertEquals("test_column", subTaskParams.getOriginTableName());
    assertEquals(1, subTaskParams.getSqlsToBeExecuted().size());
    assertEquals("ALTER TABLE test_table ADD COLUMN test_column INT;", subTaskParams.getSqlsToBeExecuted().get(0));
  }

}