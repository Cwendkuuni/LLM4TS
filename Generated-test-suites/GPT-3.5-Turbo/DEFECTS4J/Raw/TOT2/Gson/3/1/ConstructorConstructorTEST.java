import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;

import static org.junit.Assert.*;

public class ConstructorConstructorTest {
    
    private ConstructorConstructor constructorConstructor;
    
    @Before
    public void setUp() {
        Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
        this.constructorConstructor = new ConstructorConstructor(instanceCreators);
    }
    
    @Test
    public void testGet_InstanceCreatorNotNull_ReturnsObjectConstructor() {
        TypeToken<String> typeToken = TypeToken.get(String.class);
        ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
        assertNotNull(objectConstructor);
    }
    
    @Test
    public void testDefaultConstructor_NoArgs_ReturnsValidObject() {
        ObjectConstructor<String> objectConstructor = constructorConstructor.newDefaultConstructor(String.class);
        assertNotNull(objectConstructor);
        String result = objectConstructor.construct();
        assertNotNull(result);
    }
    
    @Test(expected = RuntimeException.class)
    public void testDefaultConstructor_ExceptionThrown() {
        ObjectConstructor<List<String>> objectConstructor = constructorConstructor.newDefaultConstructor(List.class);
        objectConstructor.construct();
    }
    
    @Test
    public void testDefaultImplementationConstructor_CollectionType_ReturnsValidObject() {
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ObjectConstructor<ArrayList<String>> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, ArrayList.class);
        assertNotNull(objectConstructor);
        ArrayList<String> result = objectConstructor.construct();
        assertNotNull(result);
    }
    
    @Test(expected = JsonIOException.class)
    public void testDefaultImplementationConstructor_InvalidEnumSetType_ThrowsJsonIOException() {
        Type type = new TypeToken<EnumSet<String>>() {}.getType();
        constructorConstructor.newDefaultImplementationConstructor(type, EnumSet.class).construct();
    }
    
    @Test
    public void testUnsafeAllocator_ValidType_ReturnsValidObject() {
        TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
        ObjectConstructor<List<String>> objectConstructor = constructorConstructor.newUnsafeAllocator(typeToken.getType(), List.class);
        assertNotNull(objectConstructor);
        List<String> result = objectConstructor.construct();
        assertNotNull(result);
    }
    
}