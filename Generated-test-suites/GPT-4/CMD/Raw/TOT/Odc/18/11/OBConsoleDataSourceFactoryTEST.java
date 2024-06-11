import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OBConsoleDataSourceFactoryTest {
    private ConnectionConfig connectionConfig;
    private Boolean autoCommit;
    private OBConsoleDataSourceFactory factory;

    @Before
    public void setup() {
        this.connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getHost()).thenReturn("localhost");
        when(connectionConfig.getPort()).thenReturn(3306);
        this.autoCommit = true;
        this.factory = new OBConsoleDataSourceFactory(connectionConfig, autoCommit);
    }

    @Test
    public void testGetJdbcUrl() {
        String expected = "jdbc:mysql://localhost:3306";
        assertEquals(expected, factory.getJdbcUrl());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testResetPortNegative() {
        factory.resetPort(value -> -1);
    }
  
    @Test
    public void testGetDialectType() {
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        assertEquals(DialectType.MYSQL, factory.getDialectType());
    }
}