
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.session.ConnectionSessionUtil.USER_OR_SCHEMA_FORMAT;

@RunWith(MockitoJUnitRunner.class)
public class OBConsoleDataSourceFactoryTest {

  @Mock
  private ConnectionConfig connectionConfig;


  @Test
  public void testGetUsername() {
    when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);
    when(connectionConfig.getUsername()).thenReturn("test");
    String expectedReturn = String.format(USER_OR_SCHEMA_FORMAT, "test");
    assertEquals(expectedReturn, OBConsoleDataSourceFactory.getUsername(connectionConfig));
  }

  @Test
  public void testGetPassword() {
    when(connectionConfig.getPassword()).thenReturn("test");
    assertEquals("test", OBConsoleDataSourceFactory.getPassword(connectionConfig));
  }

  @Test
  public void testGetJdbcParams() {
    Map<String, String> additionalParams = new HashMap<>();
    additionalParams.put("additionalParam", "value");
    when(connectionConfig.getJdbcUrlParameters()).thenReturn(additionalParams);

    Map<String, String> jdbcParams = OBConsoleDataSourceFactory.getJdbcParams(connectionConfig);
    assertNotNull(jdbcParams);
    assertEquals("value", jdbcParams.get("additionalParam"));
  }

  @Test
  public void testGetDefaultSchema() {
    when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
    when(connectionConfig.getDefaultSchema()).thenReturn("test");
    assertEquals("test", OBConsoleDataSourceFactory.getDefaultSchema(connectionConfig));
  }

  @Test
  public void testGetDialectType() {
    OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
    when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
    assertEquals(DialectType.MYSQL, factory.getDialectType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testResetPortInvalidPort() {
    OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(connectionConfig, true);
    DataSourceFactory.ValueMapper<Integer> mapper = value -> -1;
    factory.resetPort(mapper);
  }
}
