import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

  @Test
  public void testIsContinueOnError() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
    assertTrue(params.isContinueOnError());
  }

  @Test
  public void testGenerateSubTaskParameters() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
    params.setSqlContent("ALTER TABLE example ADD column INTEGER;");
    params.setDelimeter(";");
    params.setErrorStrategy(TaskErrorStrategy.RETRY);
    
    ConnectionConfig connectionConfig = new ConnectionConfig();
    connectionConfig.setDialectType("MySQL");
    
    List<OnlineSchemaChangeScheduleTaskParameters> subTasks = params.generateSubTaskParameters(connectionConfig, "test_schema");
    
    assertEquals(1, subTasks.size());
    assertEquals("test_schema", subTasks.get(0).getDatabaseName());
    assertEquals("example", subTasks.get(0).getOriginTableName());
    assertEquals(1, subTasks.get(0).getSqlsToBeExecuted().size());
  }
}