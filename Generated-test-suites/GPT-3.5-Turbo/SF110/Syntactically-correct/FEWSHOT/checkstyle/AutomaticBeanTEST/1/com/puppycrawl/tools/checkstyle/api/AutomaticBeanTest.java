import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.puppycrawl.tools.checkstyle.api.*;
import org.apache.commons.beanutils.converters.*;
import java.lang.reflect.*;
import org.apache.commons.beanutils.*;
import java.beans.*;
import com.google.common.collect.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AutomaticBeanTest {
    
    private AutomaticBean automaticBean;
    
    @Before
    public void setUp() {
        automaticBean = new AutomaticBean();
    }
    
    @Test
    public void testConfigure() throws CheckstyleException {
        Configuration config = Mockito.mock(Configuration.class);
        when(config.getAttributeNames()).thenReturn(new String[]{"attr1", "attr2"});
        when(config.getAttribute("attr1")).thenReturn("value1");
        when(config.getAttribute("attr2")).thenReturn("value2");
        when(config.getChildren()).thenReturn(new Configuration[]{});
        
        automaticBean.configure(config);

        verify(config, times(1)).getAttributeNames();
        verify(config, times(1)).getAttribute("attr1");
        verify(config, times(1)).getAttribute("attr2");
        verify(config, times(1)).getChildren();
    }
    
    @Test(expected = CheckstyleException.class)
    public void testConfigure_NonExistentProperty() throws CheckstyleException {
        Configuration config = Mockito.mock(Configuration.class);
        when(config.getAttributeNames()).thenReturn(new String[]{"nonExistentAttr"});
        when(config.getAttribute("nonExistentAttr")).thenReturn("value1");
        when(config.getChildren()).thenReturn(new Configuration[]{});
        
        automaticBean.configure(config);

        verify(config, times(1)).getAttributeNames();
        verify(config, times(1)).getAttribute("nonExistentAttr");
        verify(config, times(1)).getChildren();
    }
    
    @Test
    public void testContextualize() throws CheckstyleException {
        Context context = Mockito.mock(Context.class);
        when(context.getAttributeNames()).thenReturn(new HashSet<>(Arrays.asList("attr1", "attr2")));
        when(context.get("attr1")).thenReturn("value1");
        when(context.get("attr2")).thenReturn("value2");
        
        automaticBean.contextualize(context);

        verify(context, times(1)).getAttributeNames();
        verify(context, times(1)).get("attr1");
        verify(context, times(1)).get("attr2");
    }
    
    @Test
    public void testGetConfiguration() {
        assertNull(automaticBean.getConfiguration());
        
        Configuration config = Mockito.mock(Configuration.class);
        automaticBean.configure(config);
        assertEquals(config, automaticBean.getConfiguration());
    }
    
    @Test
    public void testFinishLocalSetup() throws CheckstyleException {
        automaticBean.finishLocalSetup();
    }
    
    @Test(expected = CheckstyleException.class)
    public void testSetupChild() throws CheckstyleException {
        Configuration childConf = Mockito.mock(Configuration.class);
        automaticBean.setupChild(childConf);
        
        verify(childConf, times(1)).getName();
    }
    
    @Test
    public void testSetupChild_NullChildConfig() throws CheckstyleException {
        automaticBean.setupChild(null);
    }
    
    @Test
    public void testRelaxedStringArrayConverter() {
        AutomaticBean.RelaxedStringArrayConverter converter = new AutomaticBean.RelaxedStringArrayConverter();
        Class type = String[].class;
        String value = "value1, value2, value3";
        
        Object result = converter.convert(type, value);
        
        String[] expectedArray = {"value1", "value2", "value3"};
        assertArrayEquals(expectedArray, (String[]) result);
    }
}