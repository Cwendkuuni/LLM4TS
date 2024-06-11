import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {

  private BaseSettings baseSettings;

  @Before
  public void setUp() {
    ClassIntrospector classIntrospector = new ClassIntrospector();
    AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
    VisibilityChecker<?> visibilityChecker = new VisibilityChecker<>();
    PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy();
    TypeFactory typeFactory = new TypeFactory();
    TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilder<>();
    DateFormat dateFormat = new StdDateFormat();
    HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
    Locale locale = Locale.getDefault();
    TimeZone timeZone = TimeZone.getDefault();
    Base64Variant defaultBase64 = new Base64Variant();

    baseSettings = new BaseSettings(
        classIntrospector, 
        annotationIntrospector, 
        visibilityChecker, 
        propertyNamingStrategy, 
        typeFactory, 
        typeResolverBuilder, 
        dateFormat, 
        handlerInstantiator, 
        locale, 
        timeZone, 
        defaultBase64
    );
  }

  @Test
  public void testWithClassIntrospector() {
    ClassIntrospector newClassIntrospector = new ClassIntrospector();
    
    BaseSettings updatedBaseSettings = baseSettings.withClassIntrospector(newClassIntrospector);

    assertEquals(newClassIntrospector, updatedBaseSettings.getClassIntrospector());
  }
  
  @Test
  public void testWithAnnotationIntrospector() {
    AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
    
    BaseSettings updatedBaseSettings = baseSettings.withAnnotationIntrospector(newAnnotationIntrospector);

    assertEquals(newAnnotationIntrospector, updatedBaseSettings.getAnnotationIntrospector());
  }

  @Test
  public void testWithInsertedAnnotationIntrospector() {
    AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
    
    BaseSettings updatedBaseSettings = baseSettings.withInsertedAnnotationIntrospector(newAnnotationIntrospector);

    assertEquals(newAnnotationIntrospector, updatedBaseSettings.getAnnotationIntrospector());
  }

  @Test
  public void testWithAppendedAnnotationIntrospector() {
    AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
    
    BaseSettings updatedBaseSettings = baseSettings.withAppendedAnnotationIntrospector(newAnnotationIntrospector);

    assertEquals(newAnnotationIntrospector, updatedBaseSettings.getAnnotationIntrospector());
  }
  
  @Test
  public void testWithVisibilityChecker() {
    VisibilityChecker<?> newVisibilityChecker = new VisibilityChecker<>();
    
    BaseSettings updatedBaseSettings = baseSettings.withVisibilityChecker(newVisibilityChecker);

    assertEquals(newVisibilityChecker, updatedBaseSettings.getVisibilityChecker());
  }

  @Test
  public void testWithVisibility() {
    PropertyAccessor forMethod = PropertyAccessor.FIELD;
    JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.DEFAULT;

    BaseSettings updatedBaseSettings = baseSettings.withVisibility(forMethod, visibility);

    assertEquals(visibility, updatedBaseSettings.getVisibilityChecker().getDefaultVisibility());
  }
  
  @Test
  public void testWithPropertyNamingStrategy() {
    PropertyNamingStrategy newPropertyNamingStrategy = new PropertyNamingStrategy();
    
    BaseSettings updatedBaseSettings = baseSettings.withPropertyNamingStrategy(newPropertyNamingStrategy);

    assertEquals(newPropertyNamingStrategy, updatedBaseSettings.getPropertyNamingStrategy());
  }

  @Test
  public void testWithTypeFactory() {
    TypeFactory newTypeFactory = new TypeFactory();
    
    BaseSettings updatedBaseSettings = baseSettings.withTypeFactory(newTypeFactory);

    assertEquals(newTypeFactory, updatedBaseSettings.getTypeFactory());
  }

  @Test
  public void testWithTypeResolverBuilder() {
    TypeResolverBuilder<?> newTypeResolverBuilder = new TypeResolverBuilder<>();
    
    BaseSettings updatedBaseSettings = baseSettings.withTypeResolverBuilder(newTypeResolverBuilder);

    assertEquals(newTypeResolverBuilder, updatedBaseSettings.getTypeResolverBuilder());
  }
  
  @Test
  public void testWithDateFormat() {
    DateFormat newDateFormat = new StdDateFormat();
    
    BaseSettings updatedBaseSettings = baseSettings.withDateFormat(newDateFormat);

    assertEquals(newDateFormat, updatedBaseSettings.getDateFormat());
  }

  @Test
  public void testWithHandlerInstantiator() {
    HandlerInstantiator newHandlerInstantiator = new HandlerInstantiator();
    
    BaseSettings updatedBaseSettings = baseSettings.withHandlerInstantiator(newHandlerInstantiator);

    assertEquals(newHandlerInstantiator, updatedBaseSettings.getHandlerInstantiator());
  }

  @Test
  public void testWithLocale() {
    Locale newLocale = Locale.US;
    
    BaseSettings updatedBaseSettings = baseSettings.with(newLocale);

    assertEquals(newLocale, updatedBaseSettings.getLocale());
  }

  @Test
  public void testWithTimeZone() {
    TimeZone newTimeZone = TimeZone.getTimeZone("GMT");
    
    BaseSettings updatedBaseSettings = baseSettings.with(newTimeZone);

    assertEquals(newTimeZone, updatedBaseSettings.getTimeZone());
  }

  @Test
  public void testWithBase64Variant() {
    Base64Variant newBase64Variant = new Base64Variant();
    
    BaseSettings updatedBaseSettings = baseSettings.with(newBase64Variant);

    assertEquals(newBase64Variant, updatedBaseSettings.getBase64Variant());
  }
}