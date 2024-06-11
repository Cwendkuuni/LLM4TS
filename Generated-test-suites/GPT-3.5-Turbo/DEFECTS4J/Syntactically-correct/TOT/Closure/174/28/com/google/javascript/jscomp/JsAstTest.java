
import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class JsAstTest {

    private JsAst jsAst;

    @Before
    public void setUp() {
        SourceFile sourceFile = new SourceFile("test.js", "var x = 5;");
        jsAst = new JsAst(sourceFile);
    }

    @Test
    public void testGetAstRoot() {
        AbstractCompiler compiler = new Compiler();
        Node astRoot = jsAst.getAstRoot(compiler);
        assertNotNull(astRoot);
        assertEquals(astRoot.getType(), Token.SCRIPT);
    }

    @Test
    public void testClearAst() {
        jsAst.clearAst();
        assertNull(jsAst.getAstRoot(null));
    }

    @Test
    public void testGetInputId() {
        InputId inputId = jsAst.getInputId();
        assertNotNull(inputId);
        assertEquals(inputId.getIdName(), "test.js");
    }

    @Test
    public void testGetSourceFile() {
        SourceFile sourceFile = jsAst.getSourceFile();
        assertNotNull(sourceFile);
        assertEquals(sourceFile.getName(), "test.js");
        assertEquals(sourceFile.getCode(), "var x = 5;");
    }

    @Test
    public void testSetSourceFile() {
        SourceFile newSourceFile = new SourceFile("newTest.js", "var y = 10;");
        jsAst.setSourceFile(newSourceFile);
        assertEquals(jsAst.getSourceFile().getName(), "newTest.js");
        assertEquals(jsAst.getSourceFile().getCode(), "var y = 10;");
    }
}

