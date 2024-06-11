import org.junit.Test;
import static org.junit.Assert.*;

public class JsAstTest {
    @Test
    public void testJsAstConstructor() {
        SourceFile sourceFile = new SourceFile("test.js", "function test() {}");
        JsAst jsAst = new JsAst(sourceFile);
        assertEquals("test.js", jsAst.getInputId().getId());
        assertEquals(sourceFile, jsAst.getSourceFile());
    }
}