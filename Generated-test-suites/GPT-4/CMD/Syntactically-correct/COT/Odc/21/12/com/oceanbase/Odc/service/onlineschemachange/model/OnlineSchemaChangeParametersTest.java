package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class OnlineSchemaChangeParametersTest {

  // Test the isContinueOnError method
  @Test
  public void testIsContinueOnError() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
    assertTrue(params.isContinueOnError());

    params.setErrorStrategy(TaskErrorStrategy.TERMINATE);
    assertFalse(params.isContinueOnError());
  }

  // Basic test case for generateSubTaskParameters method
  @Test
  public void testGenerateSubTaskParametersBasic() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    ConnectionConfig config = new ConnectionConfig();
    // Replace with actual connection configuration
    // Set SQL content and delimiter
    params.setSqlContent("CREATE TABLE test(id INT); DROP TABLE test;");
    params.setDelimiter(";");

    List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(config, "test");

    assertNotNull(subTaskParameters);
    assertEquals(2, subTaskParameters.size());
  }

  // Testing edge case when sqlContent is empty
  @Test(expected = UnexpectedException.class)
  public void testGenerateSubTaskParametersEmptySqlContent() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    ConnectionConfig config = new ConnectionConfig();
    params.setSqlContent(""); // Empty SQL content
    params.setDelimiter(";");

    params.generateSubTaskParameters(config, "test");
  }

  // Testing edge case when sqlContent is null
  @Test(expected = UnexpectedException.class)
  public void testGenerateSubTaskParametersNullSqlContent() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    ConnectionConfig config = new ConnectionConfig();
    params.setSqlContent(null); // Null SQL content
    params.setDelimiter(";");

    params.generateSubTaskParameters(config, "test");
  }

  // Testing edge case when delimiter is null 
  @Test(expected = UnexpectedException.class)
  public void testGenerateSubTaskParametersNullDelimiter() {
    OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
    ConnectionConfig config = new ConnectionConfig();
    params.setSqlContent("CREATE TABLE test(id INT); DROP TABLE test;");
    params.setDelimiter(null); // Null delimiter

    params.generateSubTaskParameters(config, "test");
  }
}
