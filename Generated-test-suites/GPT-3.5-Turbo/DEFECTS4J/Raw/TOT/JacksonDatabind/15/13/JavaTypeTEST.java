import org.junit.Test;
import static org.junit.Assert.*;

public class JavaTypeTest {

    //Test case for narrowing a JavaType by a subclass
    @Test
    public void testNarrowBy() {
        JavaType type = new ConcreteJavaType();
        JavaType narrowedType = type.narrowBy(SubConcreteJavaType.class);
        
        assertTrue(narrowedType.hasRawClass(SubConcreteJavaType.class));
        assertFalse(narrowedType.hasRawClass(ConcreteJavaType.class));
        
        assertTrue(narrowedType.isConcrete());
        assertFalse(narrowedType.isAbstract());
    }

    //Test case for narrowing JavaType when subclass is same as the current raw class
    @Test
    public void testNarrowBySameClass() {
        JavaType type = new ConcreteJavaType();
        JavaType narrowedType = type.narrowBy(ConcreteJavaType.class);
        
        assertSame(type, narrowedType);
    }
    
    //Test case for widening a JavaType by a superclass
    @Test
    public void testWidenBy() {
        JavaType type = new ConcreteJavaType();
        JavaType widenedType = type.widenBy(AbstractJavaType.class);
        
        assertTrue(widenedType.hasRawClass(AbstractJavaType.class));
        assertFalse(widenedType.hasRawClass(ConcreteJavaType.class));
        
        assertFalse(widenedType.isConcrete());
        assertTrue(widenedType.isAbstract());
    }

    //Test case for widening JavaType when superclass is same as the current raw class
    @Test
    public void testWidenBySameClass() {
        JavaType type = new ConcreteJavaType();
        JavaType widenedType = type.widenBy(ConcreteJavaType.class);
        
        assertSame(type, widenedType);
    }
    
    //Test case for narrowing contents of a JavaType by a content class
    @Test
    public void testNarrowContentsBy() {
        JavaType type = new ContainerJavaType();
        JavaType narrowedType = type.narrowContentsBy(SubConcreteJavaType.class);
        
        assertTrue(narrowedType.isCollectionLikeType());
        assertEquals(1, narrowedType.containedTypeCount());
        assertTrue(narrowedType.containedTypeOrUnknown(0).hasRawClass(SubConcreteJavaType.class));
    }

    //Test case for widening contents of a JavaType by a content class
    @Test
    public void testWidenContentsBy() {
        JavaType type = new ContainerJavaType();
        JavaType widenedType = type.widenContentsBy(AbstractJavaType.class);
        
        assertTrue(widenedType.isCollectionLikeType());
        assertEquals(1, widenedType.containedTypeCount());
        assertTrue(widenedType.containedTypeOrUnknown(0).hasRawClass(AbstractJavaType.class));
    }
}