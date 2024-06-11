
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DefaultNameSupplier;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeCreator;
import com.google.javascript.jscomp.ScopeCreator.ScopeCallback;
import com.google.javascript.jscomp.ScopeCreator.ScopeEntry;
import com.google.javascript.jscomp.VariableMap;
import com.google.javascript.jscomp.VariableMapCreator;
import com.google.javascript.jscomp.VariableMapEntry;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RenameLabelsTest {

    private AbstractCompiler compiler;

    @org.junit.Before
    public void setUp() throws Exception {
        compiler = new Compiler();
    }

    @org.junit.Test
    public void testProcess() {
        Supplier<String> nameSupplier = Suppliers.ofInstance("a");
        RenameLabels processLabels = new RenameLabels(compiler, nameSupplier, true);
        RenameLabels.ProcessLabels processLabelsCallback = processLabels.new ProcessLabels();

        // Create label node
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, "label1");
        labelNode.addChildToBack(nameNode);

        // Test when label is referenced
        processLabelsCallback.shouldTraverse(null, labelNode, labelNode);
        processLabelsCallback.visit(null, labelNode, labelNode);
        assertTrue(processLabelsCallback.getLabelInfo("label1").referenced);
        assertEquals("a", nameNode.getString());

        // Test when label is not referenced
        processLabelsCallback.getLabelInfo("label1").referenced = false;
        labelNode.removeChild(nameNode);
        processLabelsCallback.visit(null, labelNode, labelNode);
        assertFalse(processLabelsCallback.getLabelInfo("label1").referenced);
        assertEquals(0, labelNode.getChildCount());
    }

}
