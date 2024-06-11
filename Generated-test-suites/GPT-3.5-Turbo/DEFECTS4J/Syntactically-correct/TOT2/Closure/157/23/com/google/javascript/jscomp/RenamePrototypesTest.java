import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RenamePrototypesTest {

    private RenamePrototypes renamePrototypes;
    private AbstractCompiler compiler;
    private CompilerInput input;

    @Before
    public void setup() {
        compiler = new AbstractCompiler();
        renamePrototypes = new RenamePrototypes(compiler, true, null, null);
        input = new CompilerInput("test.js", true);
    }

    @Test
    public void testProcess_prototypeCountIncremented() {
        Node node = new Node(Node.Token.GETPROP, new Node(Node.Token.STRING, "test"), new Node(Node.Token.STRING, "prototype"));
        renamePrototypes.process(node, input);
        
        assertEquals(1, renamePrototypes.getProperty("test").prototypeCount);
    }

    @Test
    public void testProcess_objLitCountIncremented() {
        Node node = new Node(Node.Token.STRING, "test");
        renamePrototypes.process(node, input);
        
        assertEquals(1, renamePrototypes.getProperty("test").objLitCount);
    }

    @Test
    public void testProcess_propertyRefCountIncremented() {
        Node node = new Node(Node.Token.GETPROP, new Node(Node.Token.STRING, "test"), new Node(Node.Token.STRING, "name"));
        renamePrototypes.process(node, input);
        
        assertEquals(1, renamePrototypes.getProperty("name").refCount);
    }

    @Test
    public void testCanRenamePrototypeProperty_privateName() {
        RenamePrototypes.Property property = renamePrototypes.new Property("_test");
        assertEquals(true, property.canRenamePrototypeProperty());
    }

    @Test
    public void testCanRenameObjLitProperty_exportedName() {
        RenamePrototypes.Property property = renamePrototypes.new Property("test");
        assertEquals(false, property.canRenameObjLitProperty());
    }

}