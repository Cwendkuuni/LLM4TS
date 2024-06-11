package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

  private ConnectionConfig connectionConfig;
  private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

  @Before
  public void init() {
    connectionConfig = mock(ConnectionConfig.class);
    onlineSchemaChangeParameters = new OnlineSchemaChangeParameters(
        OnlineSchemaChangeSqlType.ALTER,
        "ALTER TABLE example_table ALTER COLUMN column_name TYPE new_type;",
        TaskErrorStrategy.CONTINUE,
        30,
        3,
        OriginTableCleanStrategy.DROP_AND_CREATE,
        ";",
        Arrays.asList("user1", "user2"),
        SwapTableType.SWAP_ONE_FOR_ONE
    );
  }

  @Test
  public void testOnlineSchemaChangeParametersConstruction() {
    // Test that the constructor assigns the correct values to the fields
    assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
    assertEquals("ALTER TABLE example_table ALTER COLUMN column_name TYPE new_type;", onlineSchemaChangeParameters.getSqlContent());
    assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
    assertEquals(30, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
    assertEquals(3, onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
    assertEquals(OriginTableCleanStrategy.DROP_AND_CREATE, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
    assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
    assertEquals(Arrays.asList("user1", "user2"), onlineSchemaChangeParameters.getLockUsers());
    assertEquals(SwapTableType.SWAP_ONE_FOR_ONE, onlineSchemaChangeParameters.getSwapTableType());

    // Test that the constructor initializes an empty list for the subtask parameters
    assertNotNull(onlineSchemaChangeParameters.getGenerateSubTaskParameters(connectionConfig, ""));
  }

  @Test
  public void testOnlineSchemaChangeParametersEmptyConstructor() {
    OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();

    assertEquals(null, onlineSchemaChangeParameters.getSqlType());
    assertEquals(null, onlineSchemaChangeParameters.getSqlContent());
    assertEquals(null, onlineSchemaChangeParameters.getErrorStrategy());
    assertEquals(null, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
    assertEquals(null, onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
    assertEquals(null, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
    assertEquals(null, onlineSchemaChangeParameters.getDelimiter());
    assertEquals(null, onlineSchemaChangeParameters.getLockUsers());
    assertEquals(null, onlineSchemaChangeParameters.getSwapTableType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOnlineSchemaChangeParametersNullConstructorArguments() {
    OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters(null, null, null, null, null, null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOnlineSchemaChangeParametersInvalidSqlFormat() {
    when(connectionConfig.getDialectType()).thenReturn("MySQL");
    onlineSchemaChangeParameters = new OnlineSchemaChangeParameters(
        OnlineSchemaChangeSqlType.ALTER,
        "ALTER TABLE example_table ALTER COLUMN column_name", // missing semicolon
        TaskErrorStrategy.CONTINUE,
        30,
        3,
        OriginTableCleanStrategy.DROP_AND_CREATE,
        ";",
        Arrays.asList("user1", "user2"),
        SwapTableType.SWAP_ONE_FOR_ONE
    );

    onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOnlineSchemaChangeParametersInvalidSwapTableType() {
    when(connectionConfig.getDialectType()).thenReturn("MySQL");
    onlineSchemaChangeParameters = new OnlineSchemaChangeParameters(
        OnlineSchemaChangeSqlType.ALTER,
        "ALTER TABLE example_table ALTER COLUMN column_name TYPE new_type;",
        TaskErrorStrategy.CONTINUE,
        30,
        3,
        OriginTableCleanStrategy.DROP_AND_CREATE,
        ";",
        Arrays.asList("user1", "user2"),
        SwapTableType.INVALID // Invalid SwapTableType
    );

    onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOnlineSchemaChangeParametersUnsupportedTaskErrorStrategy() {
    when(connectionConfig.getDialectType()).thenReturn("MySQL");
    onlineSchemaChangeParameters = new OnlineSchemaChangeParameters(
        OnlineSchemaChangeSqlType.ALTER,
        "ALTER TABLE example_table ALTER COLUMN column_name TYPE new_type;",
        TaskErrorStrategy.ABORT, // Unsupported TaskErrorStrategy
        30,
        3,
        OriginTableCleanStrategy.DROP_AND_CREATE,
        ";",
        Arrays.asList("user1", "user2"),
        SwapTableType.SWAP_ONE_FOR_ONE
    );

    onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "");
  }

}