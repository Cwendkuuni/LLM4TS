import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.beanutils.converters.*;
import java.beans.*;
import com.puppycrawl.tools.checkstyle.api.*;

public class AutomaticBeanTest {
    
    @Test
    public void testConfigure_typical() throws Exception {
        Configuration config = new DefaultConfiguration("typical");
        AutomaticBean automaticBean = new AutomaticBean();
        
        automaticBean.configure(config);
        
        // Assert that configuration object is assigned correctly
        assertEquals(config, automaticBean.getConfiguration());
    }
    
    @Test
    public void testConfigure_edge() throws Exception {
        Configuration config = new DefaultConfiguration("edge");
        AutomaticBean automaticBean = new AutomaticBean();
        
        automaticBean.configure(config);
        
        // Assert that configuration object is assigned correctly
        assertEquals(config, automaticBean.getConfiguration());
    }
    
    @Test(expected = CheckstyleException.class)
    public void testConfigure_error() throws Exception {
        Configuration config = new DefaultConfiguration("error");
        config.addAttribute("invalidAttribute", "value");
        AutomaticBean automaticBean = new AutomaticBean();
        
        automaticBean.configure(config);
        
        // CheckstyleException is expected to be thrown as the attribute is invalid
    }
    
    @Test
    public void testContextualize_typical() throws Exception {
        Context context = new DefaultContext();
        context.put("key", "value");
        AutomaticBean automaticBean = new AutomaticBean();
        
        automaticBean.contextualize(context);
        
        // Assert that property is copied from context successfully
        assertEquals("value", automaticBean.getValue());
    }
    
    @Test
    public void testContextualize_edge() throws Exception {
        Context context = new DefaultContext();
        context.put("key", "");
        AutomaticBean automaticBean = new AutomaticBean();
        
        automaticBean.contextualize(context);
        
        // Assert that empty value is copied from context successfully
        assertEquals("", automaticBean.getValue());
    }
    
    @Test
    public void testContextualize_error() throws Exception {
        Context context = new DefaultContext();
        // Do not put any values in context
        AutomaticBean automaticBean = new AutomaticBean();
        
        automaticBean.contextualize(context);
        
        // No properties should be copied as there are no values in context
        assertNull(automaticBean.getValue());
    }
    
    @Test
    public void testRelaxedStringArrayConverter_typical() throws Exception {
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
        
        Class<?> type = String[].class;
        Object value = "value1, value2, value3";
        
        Object result = converter.convert(type, value);
        
        // Assert that the string value is converted into array correctly
        assertTrue(result instanceof String[]);
        String[] arrayResult = (String[]) result;
        assertEquals(3, arrayResult.length);
        assertEquals("value1", arrayResult[0]);
        assertEquals("value2", arrayResult[1]);
        assertEquals("value3", arrayResult[2]);
    }
    
    @Test
    public void testRelaxedStringArrayConverter_edge() throws Exception {
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
        
        Class<?> type = String[].class;
        Object value = "";
        
        Object result = converter.convert(type, value);
        
        // Assert that the empty string value is converted into empty array
        assertTrue(result instanceof String[]);
        String[] arrayResult = (String[]) result;
        assertEquals(0, arrayResult.length);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRelaxedStringArrayConverter_error() throws Exception {
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
        
        Class<?> type = Object.class;
        Object value = "value";
        
        converter.convert(type, value);
        
        // UnsupportedOperationException is expected to be thrown as the type is not supported
    }
}