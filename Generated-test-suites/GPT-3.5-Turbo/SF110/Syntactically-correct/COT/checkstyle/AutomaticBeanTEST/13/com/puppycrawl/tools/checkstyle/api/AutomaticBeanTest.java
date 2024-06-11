
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;

    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }

    // Test configure method
    @Test
    public void testConfigure() throws CheckstyleException {
        Configuration config = new Configuration();
        automaticBean.configure(config);

        assertEquals(config, automaticBean.getConfiguration());
    }

    // Test contextualize method
    @Test
    public void testContextualize() throws CheckstyleException {
        Context context = new Context();
        context.setAttribute("attribute1", "value1");
        context.setAttribute("attribute2", "value2");

        automaticBean.contextualize(context);
    }
}
