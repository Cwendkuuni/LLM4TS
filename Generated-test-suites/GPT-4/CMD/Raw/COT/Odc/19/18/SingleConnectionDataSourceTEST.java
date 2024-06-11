package com.oceanbase.odc.core.datasource.test;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import org.junit.Test;
import static org.junit.Assert.*;

public class SingleConnectionDataSourceTest {
    
    @Test
    public void testDefaultConstructor() {
        SingleConnectionDataSource scds = new SingleConnectionDataSource();
        assertNotNull(scds);
    }

    @Test
    public void testParameterizedConstructorWithAutoReconnectTrue() {
        SingleConnectionDataSource scds = new SingleConnectionDataSource(true);
        assertNotNull(scds);
    }

    @Test
    public void testParameterizedConstructorWithAutoReconnectFalse() {
        SingleConnectionDataSource scds = new SingleConnectionDataSource(false);
        assertNotNull(scds);
    }
    
    //... Continue with other methods
}