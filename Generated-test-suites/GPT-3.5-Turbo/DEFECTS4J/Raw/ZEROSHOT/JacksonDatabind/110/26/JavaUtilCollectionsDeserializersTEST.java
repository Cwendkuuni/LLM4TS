import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.mockito.Mockito.*;

public class JavaUtilCollectionsDeserializersTest {

  private JavaUtilCollectionsDeserializers deserializers;
  private DeserializationContext deserializationContext;

  @Before
  public void setUp() {
    deserializers = new JavaUtilCollectionsDeserializers() {
      @Override
      public JavaUtilCollectionsConverter converter(int kind, JavaType concreteType, Class<?> rawSuper) {
        return mock(JavaUtilCollectionsConverter.class);
      }
    };
    deserializationContext = mock(DeserializationContext.class);
  }

  @Test
  public void testFindForCollection() throws JsonMappingException {
    JavaType type = mock(JavaType.class);
    deserializers.findForCollection(deserializationContext, type);
    verify(type, times(2)).hasRawClass(any(Class.class));
    verify(deserializationContext, times(1)).findSuperType(any(Class.class));
    verify(deserializationContext, times(1)).findSuperType(any(Class.class));
    verifyNew(StdDelegatingDeserializer.class).withArguments(any(JsonDeserializer.class));
  }

  @Test
  public void testFindForMap() throws JsonMappingException {
    JavaType type = mock(JavaType.class);
    deserializers.findForMap(deserializationContext, type);
    verify(type, times(2)).hasRawClass(any(Class.class));
    verify(deserializationContext, times(1)).findSuperType(any(Class.class));
    verifyNew(StdDelegatingDeserializer.class).withArguments(any(JsonDeserializer.class));
  }

  @Test
  public void testConverter() {
    JavaType concreteType = mock(JavaType.class);
    Class<?> rawSuper = mock(Class.class);
    deserializers.converter(1, concreteType, rawSuper);
    verify(concreteType, times(1)).findSuperType(any(Class.class));
    verifyNew(JavaUtilCollectionsConverter.class).withArguments(anyInt(), any(JavaType.class));
  }

  private static class JavaUtilCollectionsConverter implements Converter<Object, Object> {

    private final JavaType _inputType;
    private final int _kind;

    public JavaUtilCollectionsConverter(int kind, JavaType inputType) {
      _inputType = inputType;
      _kind = kind;
    }

    @Override
    public Object convert(Object value) {
      if (value == null) {
        return null;
      }
      return value;
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
      return _inputType;
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
      return _inputType;
    }
  }

}