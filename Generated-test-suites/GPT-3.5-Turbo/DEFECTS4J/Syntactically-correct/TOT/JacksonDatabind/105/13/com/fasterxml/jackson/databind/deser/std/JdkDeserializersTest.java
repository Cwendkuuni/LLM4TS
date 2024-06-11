import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.*;

public class JdkDeserializersTest {

    @Test
    public void testFind_withValidRawTypeAndValidClsName_returnsDeserializer() {
        //Arrange
        Class<?> rawType = UUID.class;
        String clsName = "java.util.UUID";
        
        //Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        //Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof UUIDDeserializer);
    }
    
    @Test
    public void testFind_withInvalidRawTypeAndValidClsName_returnsNull() {
        //Arrange
        Class<?> rawType = Integer.class;
        String clsName = "java.util.UUID";
        
        //Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        //Assert
        assertNull(deserializer);
    }
    
    @Test
    public void testFind_withInvalidRawTypeAndInvalidClsName_returnsNull() {
        //Arrange
        Class<?> rawType = Integer.class;
        String clsName = "com.example.DoesNotExist";
        
        //Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        //Assert
        assertNull(deserializer);
    }
    
    @Test
    public void testFind_withValidRawTypeAndValidClsNameButNoMatchingDeserializer_returnsNull() {
        //Arrange
        Class<?> rawType = AtomicBoolean.class;
        String clsName = "java.util.concurrent.atomic.AtomicBoolean";
        
        //Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        //Assert
        assertNull(deserializer);
    }
    
    @Test
    public void testFind_withValidRawTypeAndValidClsNameAndMatchingDeserializer_returnsDeserializer() {
        //Arrange
        Class<?> rawType = AtomicBoolean.class;
        String clsName = "java.util.concurrent.atomic.AtomicBoolean";
        
        //Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        //Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);
    }
    
    @Test
    public void testFind_withValidRawTypeAndValidClsNameAndMatchingDeserializer_returnsDeserializer2() {
        //Arrange
        Class<?> rawType = StackTraceElement.class;
        String clsName = "java.lang.StackTraceElement";
        
        //Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        //Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);
    }
    
    @Test
    public void testFind_withValidRawTypeAndValidClsNameAndMatchingDeserializer_returnsDeserializer3() {
        //Arrange
        Class<?> rawType = ByteBuffer.class;
        String clsName = "java.nio.ByteBuffer";
        
        //Act
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        
        //Assert
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);
    }
}