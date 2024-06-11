import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setUp() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.OB_ORACLE);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(1521);
        when(connectionConfig.getUsername()).thenReturn("user");
        when(connectionConfig.getPassword()).thenReturn("password");
        dataSourceFactory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetJdbcUrl() {
        String expectedUrl = "jdbc:oracle:thin:@localhost:1521:\"user\"";
        assertEquals(expectedUrl, dataSourceFactory.getJdbcUrl());
    }

    @Test
    public void testGetUsername() {
        assertEquals("\"user\"", dataSourceFactory.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", dataSourceFactory.getPassword());
    }

    @Test
    public void testGetConnectType() {
        assertEquals(ConnectType.OBCONSOLE, dataSourceFactory.getConnectType());
    }

    @Test
    public void testGetDefaultSchema() {
        assertEquals("\"user\"", dataSourceFactory.getDefaultSchema());
    }

}