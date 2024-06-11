import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.ClassIntrospector;

public class BaseSettingsTest {

  @Test
  public void test_classIntrospector() {
    BaseSettings settings = new BaseSettings(ClassIntrospector.getDefaultInstance(),
        AnnotationIntrospector.getDefault(),
        VisibilityChecker.getDefault(),
        PropertyNamingStrategy.getDefault(),
        TypeFactory.defaultInstance,
        TypeResolverBuilder.defaultInstance,
        null,
        HandlerInstantiator.getDefault(),
        Locale.getDefault(),
        TimeZone.getDefault(),
        Base64Variant.STANDARD);
    ClassIntrospector actual = settings.getClassIntrospector();
    ClassIntrospector expected = ClassIntrospector.getDefaultInstance();
    assertEquals(expected, actual);
  }
}