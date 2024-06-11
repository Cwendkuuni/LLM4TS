
import com.fasterxml.jackson.databind.cfg.BaseSettings;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker.Std;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker.Std.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setUp() {
        baseSettings = new BaseSettings(
            null, null, null, null, null,
            null, null, null, null,
            null, null
        );
    }

    @Test
    public void testWithClassIntrospector() {
        // Arrange
        ClassIntrospector ci = new ClassIntrospector();
        
        // Act
        BaseSettings result = baseSettings.withClassIntrospector(ci);
        
        // Assert
        assertNotNull(result);
        assertEquals(ci, result.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        // Arrange
        AnnotationIntrospector ai = new AnnotationIntrospector();
        
        // Act
        BaseSettings result = baseSettings.withAnnotationIntrospector(ai);
        
        // Assert
        assertNotNull(result);
        assertEquals(ai, result.getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        // Arrange
        VisibilityChecker<Std> vc = new Std(
            Visibility.ANY,
            Visibility.DEFAULT
        );
        
        // Act
        BaseSettings result = baseSettings.withVisibilityChecker(vc);
        
        // Assert
        assertNotNull(result);
        assertEquals(vc, result.getVisibilityChecker());
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        // Arrange
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        
        // Act
        BaseSettings result = baseSettings.withPropertyNamingStrategy(pns);
        
        // Assert
        assertNotNull(result);
        assertEquals(pns, result.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        // Arrange
        TypeFactory tf = new TypeFactory();
        
        // Act
        BaseSettings result = baseSettings.withTypeFactory(tf);
        
        // Assert
        assertNotNull(result);
        assertEquals(tf, result.getTypeFactory());
    }

    @Test
    public void testWithDateFormat() {
        // Arrange
        SimpleDateFormat sdf = new SimpleDateFormat();
        
        // Act
        BaseSettings result = baseSettings.withDateFormat(sdf);
        
        // Assert
        assertNotNull(result);
        assertEquals(sdf, result.getDateFormat());
    }

    @Test
    public void testWithTimeZone() {
        // Arrange
        TimeZone tz = TimeZone.getDefault();
        
        // Act
        BaseSettings result = baseSettings.withTimeZone(tz);
        
        // Assert
        assertNotNull(result);
        assertEquals(tz, result.getTimeZone());
    }

    @Test
    public void testWithDefaultBase64() {
        // Arrange
        Base64Variant defaultBase64 = new Base64Variant();
        
        // Act
        BaseSettings result = baseSettings.with(defaultBase64);
        
        // Assert
        assertNotNull(result);
        assertEquals(defaultBase64, result.getBase64Variant());
    }
}

