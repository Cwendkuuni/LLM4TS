import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import java.lang.reflect.TypeVariable;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assert(empty.isEmpty());
        assert(empty.size() == 0);
        assert(empty.getTypeParameters().isEmpty());
    }

    @Test
    public void testCreate() {
        JavaType type = JavaType.forClass(String.class);
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, type);
        assert(typeBindings.findBoundType("E") != null);
        assert(typeBindings.size() == 1);
        assert(typeBindings.getBoundName(0).equals("E"));
        assert(typeBindings.getBoundType(0).equals(type));
        assert(typeBindings.getTypeParameters().contains(type));
    }

    @Test
    public void testCreateIfNeeded() {
        JavaType type1 = JavaType.forClass(String.class);
        JavaType type2 = JavaType.forClass(Integer.class);

        TypeBindings typeBindings1 = TypeBindings.createIfNeeded(ArrayList.class, type1);
        assert(typeBindings1.findBoundType("E") != null);
        assert(typeBindings1.size() == 1);
        assert(typeBindings1.getBoundName(0).equals("E"));
        assert(typeBindings1.getBoundType(0).equals(type1));
        assert(typeBindings1.getTypeParameters().contains(type1));

        TypeBindings typeBindings2 = TypeBindings.createIfNeeded(ArrayList.class, new JavaType[]{type1, type2});
        assert(typeBindings2.findBoundType("E") != null);
        assert(typeBindings2.findBoundType("E2") != null);
        assert(typeBindings2.size() == 2);
        assert(typeBindings2.getBoundName(0).equals("E"));
        assert(typeBindings2.getBoundName(1).equals("E2"));
        assert(typeBindings2.getBoundType(0).equals(type1));
        assert(typeBindings2.getBoundType(1).equals(type2));
        assert(typeBindings2.getTypeParameters().contains(type1));
        assert(typeBindings2.getTypeParameters().contains(type2));
    }

    @Test
    public void testWithUnboundVariable() {
        TypeBindings typeBindings = TypeBindings.create(ArrayList.class, JavaType.forClass(String.class)).withUnboundVariable("T");
        assert(typeBindings.findBoundType("E") != null);
        assert(typeBindings.findBoundType("T") == null);
        assert(typeBindings.hasUnbound("T"));
    }
}