import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RenamePrototypesTest {

    @Test
    public void testProcess() {
        RenamePrototypes renamer = new RenamePrototypes(compiler, true, null, prevUsedRenameMap);
        renamer.process(externs, root);
        
        // Test property renaming mapping
        VariableMap propertyMap = renamer.getPropertyMap();
        assertEquals("newName1", propertyMap.lookupNewName("oldName1"));
        assertNull(propertyMap.lookupNewName("oldName2"));
        assertEquals("newName3", propertyMap.lookupNewName("oldName3"));
        
        // Test string node renaming
        assertEquals("newName1", stringNode1.getString());
        assertEquals("oldName2", stringNode2.getString());
        assertEquals("newName3", stringNode3.getString());
    }
}