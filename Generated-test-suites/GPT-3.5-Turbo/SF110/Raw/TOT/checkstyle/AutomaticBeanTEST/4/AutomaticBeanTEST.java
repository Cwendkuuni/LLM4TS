import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.AutomaticBean;
import org.apache.commons.beanutils.converters.*;
import java.lang.reflect.*;
import org.apache.commons.beanutils.*;
import java.beans.*;
import com.google.common.collect.*;
import java.util.*;
import org.mockito.*;

public class AutomaticBeanTest {
    
    private AutomaticBean automaticBean;
    
    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }
    
    @Test
    public void testConfigure() throws CheckstyleException {
        Configuration configuration = Mockito.mock(Configuration.class);
        automaticBean.configure(configuration);
        assertEquals(configuration, automaticBean.getConfiguration());
    }
    
    @Test(expected = CheckstyleException.class)
    public void testConfigure_InvalidProperty() throws CheckstyleException {
        // Test case for non-existing property
        Configuration configuration = Mockito.mock(Configuration.class);
        Mockito.when(configuration.getAttributeNames()).thenReturn(new String[] { "nonExistingProperty" });
        automaticBean.configure(configuration);
    }
    
    @Test(expected = CheckstyleException.class)
    public void testConfigure_Exception_Property() throws CheckstyleException {
        // Test case for exception during property copy
        Configuration configuration = Mockito.mock(Configuration.class);
        Mockito.when(configuration.getAttributeNames()).thenReturn(new String[] { "exceptionProperty" });
        Mockito.when(configuration.getAttribute("exceptionProperty")).thenReturn("value");
        automaticBean.configure(configuration);
    }
    
    @Test(expected = CheckstyleException.class)
    public void testConfigure_IllegalValue_Property() throws CheckstyleException {
        // Test case for illegal value for property
        Configuration configuration = Mockito.mock(Configuration.class);
        Mockito.when(configuration.getAttributeNames()).thenReturn(new String[] { "illegalValueProperty" });
        Mockito.when(configuration.getAttribute("illegalValueProperty")).thenReturn("illegalValue");
        automaticBean.configure(configuration);
    }
    
    @Test
    public void testContextualize() throws CheckstyleException {
        Context context = Mockito.mock(Context.class);
        automaticBean.contextualize(context);
    }
    
    @Test(expected = CheckstyleException.class)
    public void testContextualize_InvalidProperty() throws CheckstyleException {
        // Test case for non-existing property during contextualization
        Context context = Mockito.mock(Context.class);
        Mockito.when(context.getAttributeNames()).thenReturn(new HashSet<String>(Arrays.asList("nonExistingProperty")));
        automaticBean.contextualize(context);
    }
    
    @Test(expected = CheckstyleException.class)
    public void testContextualize_Exception_Property() throws CheckstyleException {
        // Test case for exception during property copy during contextualization
        Context context = Mockito.mock(Context.class);
        Mockito.when(context.getAttributeNames()).thenReturn(new HashSet<String>(Arrays.asList("exceptionProperty")));
        Mockito.when(context.get("exceptionProperty")).thenReturn("value");
        automaticBean.contextualize(context);
    }
    
    @Test
    public void testFinishLocalSetup() throws CheckstyleException {
        automaticBean.finishLocalSetup();
    }
    
    @Test(expected = CheckstyleException.class)
    public void testSetupChild() throws CheckstyleException {
        Configuration childConfig = Mockito.mock(Configuration.class);
        automaticBean.setupChild(childConfig);
    }
    
    @Test
    public void testSetupChild_NullChildConfig() throws CheckstyleException {
        automaticBean.setupChild(null);
    }
}