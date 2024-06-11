import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.util.*;
import com.fasterxml.jackson.databind.JsonMappingException;

public class JavaUtilCollectionsDeserializersTest {

    @Test
	public void testFindForCollection() throws JsonMappingException {
	    // Arrange
	    DeserializationContext ctxt = null;
	    JavaType type = null;

	    // Act
	    JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForCollection(ctxt, type);

	    // Assert
	    assertNull(result);
	}

    @Test
	public void testFindForMap() throws JsonMappingException {
	    // Arrange
	    DeserializationContext ctxt = null;
	    JavaType type = null;

	    // Act
	    JsonDeserializer<?> result = JavaUtilCollectionsDeserializers.findForMap(ctxt, type);

	    // Assert
	    assertNull(result);
	}

    @Test
	public void testConverter() {
	    // Arrange
	    int kind = 0;
	    JavaType concreteType = null;
	    Class<?> rawSuper = null;

	    // Act
	    JavaUtilCollectionsDeserializers.JavaUtilCollectionsConverter result = JavaUtilCollectionsDeserializers.converter(kind, concreteType, rawSuper);

	    // Assert
	    assertNotNull(result);
	}


}