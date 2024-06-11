package com.oceanbase.odc.plugin.connect.obmysql;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.Connection;
import java.sql.SQLException;
import com.oceanbase.jdbc.OceanBaseConnection;
import com.oceanbase.odc.plugin.connect.obmysql.OBMySQLSessionExtension;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

public class OBMySQLSessionExtensionTest {

    @Test
    public void testKillQuery() {
      // ...
    }

    @Test
    public void testSwitchSchema() throws SQLException {
      // ...
    }

    @Test
    public void testGetCurrentSchema() {
      // ...
    }

    @Test
    public void testGetConnectionId() {
      // ...
    }

    @Test(expected = SQLException.class)
    public void testSwitchSchemaException() throws SQLException {
      // ...
    }

    @Test
    public void testGetConnectionIdException() {
      // ...
    }
}