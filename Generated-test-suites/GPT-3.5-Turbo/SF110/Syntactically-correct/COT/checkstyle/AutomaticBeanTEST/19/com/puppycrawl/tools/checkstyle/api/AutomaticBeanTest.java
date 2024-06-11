import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.*;
import org.apache.commons.beanutils.ConversionException;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import static org.junit.Assert.*;

public class AutomaticBeanTest {
    private AutomaticBean automaticBean;
  
    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }
    
    @Test
    public void testConfigure() throws CheckstyleException {
        Configuration config = new DefaultConfiguration("TestModule");
        automaticBean.configure(config);
        assertEquals(config, automaticBean.getConfiguration());
    }
    
    @Test
    public void testConfigure_AttributeDoesNotExist() {
        Configuration config = new DefaultConfiguration("TestModule");
        config.addAttribute("key", "value");
        CheckstyleException exception = assertThrows(CheckstyleException.class, () -> automaticBean.configure(config));
        assertEquals("Property 'key' in module TestModule does not exist, please check the documentation", exception.getMessage());
    }
    
    @Test
    public void testConfigure_CannotSetProperty() {
        Configuration config = new DefaultConfiguration("TestModule");
        config.addAttribute("key", "value");
        automaticBean = new AutomaticBean() {
            @Override
            protected void tryCopyProperty(String moduleName, String key, Object value, boolean recheck) throws CheckstyleException {
                throw new InvocationTargetException(new IllegalAccessException());
            }
        };
        CheckstyleException exception = assertThrows(CheckstyleException.class, () -> automaticBean.configure(config));
        assertEquals("Cannot set property 'key' to 'value' in module TestModule", exception.getMessage());
    }
    
    @Test
    public void testConfigure_IllegalValue() {
        Configuration config = new DefaultConfiguration("TestModule");
        config.addAttribute("key", "value");
        automaticBean = new AutomaticBean() {
            @Override
            protected void tryCopyProperty(String moduleName, String key, Object value, boolean recheck) throws CheckstyleException {
                throw new ConversionException();
            }
        };
        CheckstyleException exception = assertThrows(CheckstyleException.class, () -> automaticBean.configure(config));
        assertEquals("illegal value 'value' for property 'key' of module TestModule", exception.getMessage());
    }
    
    @Test
    public void testContextualize() throws CheckstyleException {
        Context context = new SimpleContext();
        context.setAttribute("key", "value");
        automaticBean.contextualize(context);
    }
    
    @Test
    public void testContextualize_CannotSetProperty() {
        Context context = new SimpleContext();
        context.setAttribute("key", "value");
        automaticBean = new AutomaticBean() {
            @Override
            protected void tryCopyProperty(String moduleName, String key, Object value, boolean recheck) throws CheckstyleException {
                throw new InvocationTargetException(new IllegalAccessException());
            }
        };
        CheckstyleException exception = assertThrows(CheckstyleException.class, () -> automaticBean.contextualize(context));
        assertEquals("Cannot set property 'key' to 'value' in module com.puppycrawl.tools.checkstyle.api.AutomaticBean", exception.getMessage());
    }
    
    @Test
    public void testGetConfiguration() {
        Configuration config = new DefaultConfiguration("TestModule");
        automaticBean = new AutomaticBean() {
            @Override
            protected void tryCopyProperty(String moduleName, String key, Object value, boolean recheck) throws CheckstyleException {
                throw new InvocationTargetException(new IllegalAccessException());
            }
        };
        automaticBean.configure(config);
        assertEquals(config, automaticBean.getConfiguration());
    }
    
    @Test
    public void testFinishLocalSetup() throws CheckstyleException {
        automaticBean.finishLocalSetup();
    }
    
    @Test
    public void testSetupChild() throws CheckstyleException {
        Configuration childConfig = new DefaultConfiguration("ChildModule");
        CheckstyleException exception = assertThrows(CheckstyleException.class, () -> automaticBean.setupChild(childConfig));
        assertEquals("ChildModule is not allowed as a child in null", exception.getMessage());
    }
    
    @Test
    public void testRelaxedStringArrayConverter() {
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
        Object result = converter.convert(String[].class, "value1, value2, value3");
        assertTrue(result instanceof String[]);
        String[] arrayResult = (String[]) result;
        assertArrayEquals(new String[]{"value1", "value2", "value3"}, arrayResult);
    }
}

