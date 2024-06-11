
package org.objectweb.asm.jip.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.jip.MethodAdapter;

public class LocalVariablesSorterTest {
    
    @Test
    public void testConstructor() {
        MethodVisitor mv = new MethodVisitor(0);
        LocalVariablesSorter lvs = new LocalVariablesSorter(0, "desc", mv);
        assertEquals(0, lvs.mapping.length);
        assertEquals(0, lvs.newLocals.length);
        assertEquals(1, lvs.nextLocal);
        assertEquals(1, lvs.firstLocal);
    }
    
    // TODO: Add test cases for other methods of LocalVariablesSorter

}
