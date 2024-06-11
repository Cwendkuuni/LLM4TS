import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ConstructorConstructorTest {

    private ConstructorConstructor constructorConstructor;

    @Before
    public void setUp() {
        constructorConstructor = new ConstructorConstructor(new HashMap<>());
    }

    @Test
    public void get_Test() {
        TypeToken<Object> typeToken = TypeToken.get(Object.class);
        ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken);

        assertNotNull(objectConstructor);
    }

    @Test
    public void newDefaultConstructor_Test() {
        Class<Object> rawType = Object.class;
        ObjectConstructor<Object> objectConstructor = constructorConstructor.newDefaultConstructor(rawType);

        assertNotNull(objectConstructor);
    }

    @Test
    public void newDefaultImplementationConstructor_Test() {
        Type type = new TypeToken<Object>(){}.getType();
        Class<Object> rawType = Object.class;
        ObjectConstructor<Object> objectConstructor = constructorConstructor.newDefaultImplementationConstructor(type, rawType);

        assertNotNull(objectConstructor);
    }

    @Test
    public void newUnsafeAllocator_Test() {
        Type type = new TypeToken<Object>(){}.getType();
        Class<Object> rawType = Object.class;
        ObjectConstructor<Object> objectConstructor = constructorConstructor.newUnsafeAllocator(type, rawType);

        assertNotNull(objectConstructor);
    }
}