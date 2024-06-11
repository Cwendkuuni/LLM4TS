
import com.fasterxml.jackson.databind.JavaType;
import java.lang.reflect.TypeVariable;
import org.junit.Test;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings bindings = TypeBindings.emptyBindings();
        assert(bindings.isEmpty());
        assert(bindings.size() == 0);
    }

    @Test
    public void testFindBoundType() {
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, JavaType.class);
        JavaType boundType = bindings.findBoundType("T");
        assert(boundType.equals(JavaType.class));
    }

    @Test
    public void testSize() {
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, JavaType.class);
        assert(bindings.size() == 1);
    }

    @Test
    public void testGetBoundName() {
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, JavaType.class);
        String boundName = bindings.getBoundName(0);
        assert(boundName.equals("T"));
    }

    @Test
    public void testGetBoundType() {
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, JavaType.class);
        JavaType boundType = bindings.getBoundType(0);
        assert(boundType.equals(JavaType.class));
    }

    @Test
    public void testGetTypeParameters() {
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, JavaType.class);
        assert(bindings.getTypeParameters().get(0).equals(JavaType.class));
    }

    @Test
    public void testHasUnbound() {
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, JavaType.class);
        assert(bindings.hasUnbound("T") == false);
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, JavaType.class);
        TypeBindings newBindings = bindings.withUnboundVariable("T");
        assert(newBindings.hasUnbound("T"));
    }

    @Test
    public void testToString() {
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, JavaType.class);
        String str = bindings.toString();
        assert(str.equals("<JavaType>"));
    }

    @Test
    public void testHashCode() {
        TypeBindings bindings = TypeBindings.create(TypeBindings.class, JavaType.class);
        int hashCode = bindings.hashCode();
        assert(hashCode != 0);
    }

    @Test
    public void testEquals() {
        TypeBindings bindings1 = TypeBindings.create(TypeBindings.class, JavaType.class);
        TypeBindings bindings2 = TypeBindings.create(TypeBindings.class, JavaType.class);
        assert(bindings1.equals(bindings2));
    }
}
