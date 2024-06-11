
import org.junit.Test;
import com.fasterxml.jackson.databind.type.*;
import org.springframework.util.Assert;
import java.util.List;
import java.util.Set;

public class CollectionTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = List.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType elemT = JavaType.qualifiedTypeName(String.class.getName());
        CollectionType collectionType = CollectionType.construct(rawType, bindings, superClass, superInts, elemT);
        Assert.isInstanceOf(CollectionType.class, collectionType);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = JavaType.qualifiedTypeName(Integer.class.getName());
        CollectionType collectionType = new CollectionType(_class, _bindings, _superClass, _superInterfaces,
                contentType, _valueHandler, _typeHandler, _asStatic);
        JavaType result = collectionType.withContentType(contentType);
        Assert.isTrue(result == collectionType);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        CollectionType collectionType = new CollectionType(_class, _bindings,
                _superClass, _superInterfaces, _elementType, _valueHandler, h, _asStatic);
        CollectionType result = collectionType.withTypeHandler(h);
        Assert.isTrue(result == collectionType);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        CollectionType collectionType = new CollectionType(_class, _bindings,
                _superClass, _superInterfaces, _elementType.withTypeHandler(h),
                _valueHandler, _typeHandler, _asStatic);
        CollectionType result = collectionType.withContentTypeHandler(h);
        Assert.isTrue(result == collectionType);
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        CollectionType collectionType = new CollectionType(_class, _bindings,
                _superClass, _superInterfaces, _elementType, h, _typeHandler, _asStatic);
        CollectionType result = collectionType.withValueHandler(h);
        Assert.isTrue(result == collectionType);
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        CollectionType collectionType = new CollectionType(_class, _bindings,
                _superClass, _superInterfaces, _elementType.withValueHandler(h),
                _valueHandler, _typeHandler, _asStatic);
        CollectionType result = collectionType.withContentValueHandler(h);
        Assert.isTrue(result == collectionType);
    }

    @Test
    public void testWithStaticTyping() {
        CollectionType collectionType = new CollectionType(_class, _bindings,
                _superClass, _superInterfaces, _elementType, _valueHandler, _typeHandler, false);
        CollectionType result = collectionType.withStaticTyping();
        Assert.isTrue(result != collectionType);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = List.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType elementType = JavaType.qualifiedTypeName(String.class.getName());
        CollectionType collectionType = new CollectionType(rawType, bindings,
                superClass, superInterfaces, elementType,
                null, null, false);
        Class<?> newRawType = Set.class;
        TypeBindings newBindings = null;
        JavaType newSuperClass = null;
        JavaType[] newSuperInterfaces = null;
        JavaType newElementType = JavaType.qualifiedTypeName(Integer.class.getName());
        JavaType result = collectionType.refine(newRawType, newBindings, newSuperClass, newSuperInterfaces);
        Assert.isTrue(result != collectionType);
    }
}
