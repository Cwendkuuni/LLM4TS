
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DatabindContextTest {
   
    @Test
    public void testGetConfig() {
        DatabindContext context = mock(DatabindContext.class, CALLS_REAL_METHODS);
        MapperConfig config = mock(MapperConfig.class);
        doReturn(config).when(context).getConfig();
        
        assertEquals(config, context.getConfig());
    }
    
    @Test
    public void testGetAnnotationIntrospector() {
        DatabindContext context = mock(DatabindContext.class, CALLS_REAL_METHODS);
        MapperConfig config = mock(MapperConfig.class);
        doReturn(config).when(context).getConfig();
        doReturn(mockAnnotationIntrospector()).when(config).getAnnotationIntrospector();
        
        assertNotNull(context.getAnnotationIntrospector());
    }
    
    @Test
    public void testIsEnabled() {
        DatabindContext context = mock(DatabindContext.class, CALLS_REAL_METHODS);
        MapperConfig config = mock(MapperConfig.class);
        doReturn(config).when(context).getConfig();
        doReturn(true).when(config).isEnabled(MapperFeature.AUTO_DETECT_FIELDS);
        
        assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_FIELDS));
    }
    
    @Test
    public void testCanOverrideAccessModifiers() {
        DatabindContext context = mock(DatabindContext.class, CALLS_REAL_METHODS);
        MapperConfig config = mock(MapperConfig.class);
        doReturn(config).when(context).getConfig();
        doReturn(true).when(config).canOverrideAccessModifiers();
        
        assertTrue(context.canOverrideAccessModifiers());
    }
    
    @Test
    public void testGetActiveView() {
        DatabindContext context = mock(DatabindContext.class, CALLS_REAL_METHODS);
        MapperConfig config = mock(MapperConfig.class);
        doReturn(config).when(context).getConfig();
        doReturn(null).when(config).getActiveView();
        
        assertNull(context.getActiveView());
    }
    
    @Test
    public void testGetLocale() {
        DatabindContext context = mock(DatabindContext.class, CALLS_REAL_METHODS);
        MapperConfig config = mock(MapperConfig.class);
        doReturn(config).when(context).getConfig();
        doReturn(Locale.US).when(config).getLocale();
        
        assertEquals(Locale.US, context.getLocale());
    }
    
    @Test
    public void testGetTimeZone() {
        DatabindContext context = mock(DatabindContext.class, CALLS_REAL_METHODS);
        MapperConfig config = mock(MapperConfig.class);
        doReturn(config).when(context).getConfig();
        doReturn(TimeZone.getTimeZone("GMT")).when(config).getTimeZone();
        
        assertEquals(TimeZone.getTimeZone("GMT"), context.getTimeZone());
    }
    
    @Test
    public void testGetDefaultPropertyFormat() {
        DatabindContext context = mock(DatabindContext.class, CALLS_REAL_METHODS);
        MapperConfig config = mock(MapperConfig.class);
        doReturn(config).when(context).getConfig();
        doReturn(JsonFormat.Value.empty()).when(config).getDefaultPropertyFormat(String.class);
        
        assertNotNull(context.getDefaultPropertyFormat(String.class));
    }

    private AnnotationIntrospector mockAnnotationIntrospector() {
        AnnotationIntrospector ai = mock(AnnotationIntrospector.class);
        return ai;
    }
}
