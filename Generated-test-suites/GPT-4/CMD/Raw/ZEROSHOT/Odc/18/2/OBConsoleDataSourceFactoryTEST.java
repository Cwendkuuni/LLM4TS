
package com.oceanbase.odc.service.session.factory;

import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class OBConsoleDataSourceFactoryTest {

    private OBConsoleDataSourceFactory factory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp(){
        connectionConfig = mock(ConnectionConfig.class);
        factory = new OBConsoleDataSourceFactory(connectionConfig, true);
    }

    @Test
    public void testGetDataSource() {
        factory.getDataSource();

        verify(connectionConfig, atLeastOnce()).getDialectType();
    }
    
    @Test
    public void testResetUsername() {
        DataSourceFactory.ValueMapper<String> mapper = Mockito.mock(DataSourceFactory.ValueMapper.class);
        when(mapper.map(anyString())).thenReturn("newUsername");

        factory.resetUsername(mapper);

        assertEquals("newUsername", factory.getUsername());
    }

    @Test
    public void testResetPassword() {
        DataSourceFactory.ValueMapper<String> mapper = Mockito.mock(DataSourceFactory.ValueMapper.class);
        when(mapper.map(anyString())).thenReturn("newPassword");

        factory.resetPassword(mapper);

        assertEquals("newPassword", factory.getPassword());
    }

    @Test
    public void testResetHost() {
        DataSourceFactory.ValueMapper<String> mapper = Mockito.mock(DataSourceFactory.ValueMapper.class);
        when(mapper.map(anyString())).thenReturn("newHost");

        factory.resetHost(mapper);

        assertEquals("newHost", factory.getHost());
    }

    @Test
    public void testResetPort() {
        DataSourceFactory.ValueMapper<Integer> mapper = Mockito.mock(DataSourceFactory.ValueMapper.class);
        when(mapper.map(anyInt())).thenReturn(8080);

        factory.resetPort(mapper);

        assertEquals(new Integer(8080), factory.getPort());
    }

    @Test
    public void testResetSchema() {
        DataSourceFactory.ValueMapper<String> mapper = Mockito.mock(DataSourceFactory.ValueMapper.class);
        when(mapper.map(anyString())).thenReturn("newSchema");

        factory.resetSchema(mapper);

        assertEquals("newSchema", factory.getDefaultSchema());
    }

    @Test
    public void testResetParameters() {
        DataSourceFactory.ValueMapper<Map<String, String>> mapper = Mockito.mock(DataSourceFactory.ValueMapper.class);
        when(mapper.map(any())).thenReturn(Collections.singletonMap("key", "value"));

        factory.resetParameters(mapper);

        assertTrue(factory.getParameters("_parameters").containsKey("key"));
    }

    @Test
    public void testDeepCopy() {
        OBConsoleDataSourceFactory copy = factory.deepCopy();

        assertEquals(factory.getUsername(), copy.getUsername());
        assertEquals(factory.getPassword(), copy.getPassword());
        assertEquals(factory.getHost(), copy.getHost());
        assertEquals(factory.getPort(), copy.getPort());
        assertEquals(factory.getDefaultSchema(), copy.getDefaultSchema());
        assertTrue(factory.getParameters().equals(copy.getParameters()));
    }

}
