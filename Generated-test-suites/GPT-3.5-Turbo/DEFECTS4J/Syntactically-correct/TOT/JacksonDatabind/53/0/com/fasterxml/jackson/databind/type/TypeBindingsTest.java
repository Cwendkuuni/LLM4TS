import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings empty = TypeBindings.emptyBindings();
        assertTrue(empty.isEmpty());
        assertEquals(0, empty.size());
        assertNull(empty.getBoundName(0));
        assertNull(empty.getBoundType(0));
        assertEquals(0, empty.getTypeParameters().size());
        assertFalse(empty.hasUnbound("unbound"));
    }

    @Test
    public void testCreateWithErasedTypeAndTypeList() {
        List<JavaType> typeList = new ArrayList<>();
        typeList.add(JavaTypePanelBuilder.aNewJavaType().withName("Integer").build());
        typeList.add(JavaTypePanelBuilder.aNewJavaType().withName("String").build());

        TypeBindings bindings = TypeBindings.create(Map.class, typeList);
        assertEquals(2, bindings.size());
        assertEquals("Map", bindings.getBoundName(0));
        assertEquals("java.lang.Integer", bindings.getBoundType(0).getName());
        assertEquals("String", bindings.getBoundName(1));
        assertEquals("java.lang.String", bindings.getBoundType(1).getName());
        assertEquals(2, bindings.getTypeParameters().size());
        assertTrue(bindings.getTypeParameters().contains(JavaTypePanelBuilder.aNewJavaType().withName("Integer").build()));
        assertTrue(bindings.getTypeParameters().contains(JavaTypePanelBuilder.aNewJavaType().withName("String").build()));
        assertFalse(bindings.hasUnbound("unbound"));
    }

    @Test
    public void testCreateWithErasedTypeAndTypes() {
        JavaType type1 = JavaTypePanelBuilder.aNewJavaType().withName("Integer").build();
        JavaType type2 = JavaTypePanelBuilder.aNewJavaType().withName("String").build();

        TypeBindings bindings = TypeBindings.create(Map.class, type1, type2);
        assertEquals(2, bindings.size());
        assertEquals("Map", bindings.getBoundName(0));
        assertEquals("java.lang.Integer", bindings.getBoundType(0).getName());
        assertEquals("String", bindings.getBoundName(1));
        assertEquals("java.lang.String", bindings.getBoundType(1).getName());
        assertEquals(2, bindings.getTypeParameters().size());
        assertTrue(bindings.getTypeParameters().contains(JavaTypePanelBuilder.aNewJavaType().withName("Integer").build()));
        assertTrue(bindings.getTypeParameters().contains(JavaTypePanelBuilder.aNewJavaType().withName("String").build()));
        assertFalse(bindings.hasUnbound("unbound"));
    }

    // Add more test cases here...

}