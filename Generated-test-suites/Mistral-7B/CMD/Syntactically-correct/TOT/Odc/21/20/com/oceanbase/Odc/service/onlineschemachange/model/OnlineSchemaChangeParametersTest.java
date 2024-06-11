import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class OnlineSchemaChangeParametersTest {

  @Test
  public void testValidOnlineSchemaChangeParameters() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
    params.setsSqlContent("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);");
    params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
    params.setLockTableTimeOutSeconds(30);
    params.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
    assertNotNull(params);
  }

  @Test
  public void testInvalidOnlineSchemaChangeParameters_EmptySqlContent() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
    params.setsSqlContent("");
    try {
      params.validate();
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testInvalidOnlineSchemaChangeParameters_NullSqlContent() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
    params.setsSqlContent(null);
    try {
      params.validate();
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testInvalidOnlineSchemaChangeParameters_InvalidSqlType() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    params.setSqlType(null);
    try {
      params.validate();
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testGenerateSubTaskParameters_ValidParameters() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
    params.setsSqlContent("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);");
    params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
    params.setLockTableTimeOutSeconds(30);
    params.setSwapTableType(SwapTableType.SWAP_IN_PLACE);

    ConnectionConfig connectionConfig = new ConnectionConfig();
    String schema = "test";

    List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, schema);
    assertNotNull(subTaskParameters);
    assertEquals(1, subTaskParameters.size());

    OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
    assertEquals("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);", taskParameter.getSqlContent());
    assertEquals(OnlineSchemaChangeSqlType.CREATE, taskParameter.getSqlType());
  }

  @Test
  public void testGenerateSubTaskParameters_EmptySqlContent() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
    params.setsSqlContent("");

    ConnectionConfig connectionConfig = new ConnectionConfig();
    String schema = "test";

    List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, schema);
    assertNull(subTaskParameters);
  }

  @Test
  public void testGenerateSubTaskParameters_NullSqlContent() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
    params.setsSqlContent(null);

    ConnectionConfig connectionConfig = new ConnectionConfig();
    String schema = "test";

    List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, schema);
    assertNull(subTaskParameters);
  }

  @Test
  public void testGenerateSubTaskParameters_EmptySqls() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
    params.setsSqlContent("ALTER TABLE test_table DROP COLUMN id;");

    ConnectionConfig connectionConfig = new ConnectionConfig();
    String schema = "test";

    try {
      params.generateSubTaskParameters(connectionConfig, schema);
      fail("Expected UnexpectedException");
    } catch (UnexpectedException e) {
      // Expected
    }
  }
}