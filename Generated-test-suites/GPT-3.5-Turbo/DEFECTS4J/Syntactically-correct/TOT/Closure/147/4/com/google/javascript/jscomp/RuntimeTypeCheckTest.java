import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.Result;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class RuntimeTypeCheckTest {

    private CompilerOptions compilerOptions;

    @Before
    public void setUp() {
        compilerOptions = new CompilerOptions();
    }

    @Test
    public void testAddMarkers() {
        Compiler compiler = new Compiler();
        String testCode = "class Foo {}";
        String expectedCode = "class Foo {\n" +
                "  constructor() {\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "Foo.prototype['instance_of__Foo'] = true;";
        String actualCode = compileCode(compiler, testCode);
        Assert.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testAddChecks() {
        Compiler compiler = new Compiler();
        String testCode = "function add(a, b) {\n" +
                "  return a + b;\n" +
                "}";
        String expectedCode = "function add(a, b) {\n" +
                "  checkType(a, [valueChecker('number')]);\n" +
                "  checkType(b, [valueChecker('number')]);\n" +
                "  return checkType(a + b, [valueChecker('number')]);\n" +
                "}";
        String actualCode = compileCode(compiler, testCode);
        Assert.assertEquals(expectedCode, actualCode);
    }

    @Test
    public void testProcess() {
        Compiler compiler = new Compiler();
        String testCode = "function add(a, b) {\n" +
                "  return a + b;\n" +
                "}";
        String expectedCode = "function add(a, b) {\n" +
                "  checkType(a, [valueChecker('number')]);\n" +
                "  checkType(b, [valueChecker('number')]);\n" +
                "  return checkType(a + b, [valueChecker('number')]);\n" +
                "}\n" +
                "\n" +
                "checkType(add, [valueChecker('function'), classChecker('Function')]);";
        String actualCode = compileCode(compiler, testCode);
        Assert.assertEquals(expectedCode, actualCode);
    }

    private String compileCode(Compiler compiler, String testCode) {
        compilerOptions.setLanguageOut(CompilerOptions.LanguageMode.ECMASCRIPT6);
        compiler.initOptions(compilerOptions);
        compiler.compile(SourceFile.fromCode("testCode", testCode), SourceFile.fromCode("externs", ""));
        Result result = compiler.getResult();
        if (result.success) {
            return compiler.toSource();
        } else {
            for (JSError error : result.errors) {
                System.err.println(error.toString());
            }
            return null;
        }
    }

    private String readFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return contentBuilder.toString();
    }

    private void writeFile(String filePath, String content) {
        try {
            Path path = Paths.get(filePath);
            Files.write(path, content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}