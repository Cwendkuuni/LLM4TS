package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class AutomaticBeanTest {
    private AutomaticBean automaticBean;
    
    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }
    
    @Test
    public void testConfigure() {
        // Test case 1
        Configuration config = new Configuration();
        automaticBean.configure(config);
        assertNull(automaticBean.getConfiguration());
        
        // Test case 2
        Configuration config2 = new Configuration();
        config2.setAttribute("attr1", "value1");
        automaticBean.configure(config2);
        assertEquals(config2, automaticBean.getConfiguration());
        
        // Test case 3
        Configuration config3 = new Configuration();
        config3.setAttribute("attr2", "value2");
        Configuration childConfig = new Configuration();
        config3.addChild(childConfig);
        automaticBean.configure(config3);
        assertEquals(config3, automaticBean.getConfiguration());
    }
    
    @Test
    public void testContextualize() {
        // Test case 1
        Context context = new Context();
        automaticBean.contextualize(context);
        assertNull(automaticBean.getConfiguration());
        
        // Test case 2
        Context context2 = new Context();
        context2.setAttribute("attr1", "value1");
        automaticBean.contextualize(context2);
        assertEquals(context2, automaticBean.getConfiguration());
        
        // Test case 3
        Context context3 = new Context();
        context3.setAttribute("attr2", "value2");
        automaticBean.contextualize(context3);
        assertEquals(context3, automaticBean.getConfiguration());
    }
 
}