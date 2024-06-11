import org.junit.Before;
import org.junit.Test;
import java.beans.*;
import java.util.*;

import static org.junit.Assert.*;

public class AutomaticBeanTest {

    private AutomaticBean automaticBean;

    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }

    @Test
    public void testConfigure() throws CheckstyleException {
        Configuration configuration = new Configuration();
        configuration.addAttribute("key", "value");
        configuration.addAttribute("intKey", "100");
        configuration.addChild(new Configuration("child"));
        
        automaticBean.configure(configuration);

        assertEquals(configuration, automaticBean.getConfiguration());
        assertEquals("value", automaticBean.getConfiguration().getAttribute("key"));
        assertEquals("100", automaticBean.getConfiguration().getAttribute("intKey"));
        assertEquals("child", automaticBean.getConfiguration().getChildren()[0].getName());
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_InvalidProperty() throws CheckstyleException {
        Configuration configuration = new Configuration();
        configuration.addAttribute("invalidProperty", "value");

        automaticBean.configure(configuration);
    }

    @Test(expected = CheckstyleException.class)
    public void testConfigure_InvalidValue() throws CheckstyleException {
        Configuration configuration = new Configuration();
        configuration.addAttribute("key", "invalidValue");

        automaticBean.configure(configuration);
    }
    
    @Test(expected = CheckstyleException.class)
    public void testConfigure_ChildNotAllowed() throws CheckstyleException {
        Configuration configuration = new Configuration();
        configuration.addChild(new Configuration("child"));

        automaticBean.configure(configuration);
    }

    @Test
    public void testContextualize() throws CheckstyleException {
        Context context = new Context();

        context.put("key", "value");
        context.put("intKey", 100);

        automaticBean.contextualize(context);

        assertEquals("value", automaticBean.getConfiguration().getAttribute("key"));
        assertEquals("100", automaticBean.getConfiguration().getAttribute("intKey"));
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualize_InvalidValue() throws CheckstyleException {
        Context context = new Context();
        context.put("key", "invalidValue");

        automaticBean.contextualize(context);
    }

    @Test(expected = CheckstyleException.class)
    public void testContextualize_InvalidProperty() throws CheckstyleException {
        Context context = new Context();
        context.put("invalidProperty", "value");

        automaticBean.contextualize(context);
    }

    @Test
    public void testFinishLocalSetup() throws CheckstyleException {
        automaticBean.finishLocalSetup();
        // Add assertions if necessary
    }

    @Test(expected = CheckstyleException.class)
    public void testSetupChild_InvalidChild() throws CheckstyleException {
        automaticBean.setupChild(new Configuration("invalidChild"));
    }

}