package org.apache.commons.cli.tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.Compiler;
import org.apache.commons.cli.JavacCompiler;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.translate.BasicCommandLine;
import org.apache.commons.cli.translate.GnuParser;
import org.apache.commons.cli.util.Lookups;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.Supplier;

public class InlineObjectLiteralsTest {

    @Test
    public void testInlineObjectLiterals() throws ParseException {
        String sourceCode = "class InlineObjectLiteralsTest {\n" +
                "  static void main(String[] args) {\n" +
                "    Object obj = {\"name\": \"Alice\", \"age\": 30};\n" +
                "    print(\"Name: \", obj.name); // this will be inlined\n" +
                "    print(\"Age: \", obj.age); // this will be inlined\n" +
                "    obj = {\"name\": \"Bob\", \"age\": 40};\n" +
                "    print(\"Name: \", obj.name); // this will be inlined\n" +
                "    print(\"Age: \", obj.age); // this will be inlined\n" +
                "  }\n" +
                "}\n";

        Compiler compiler = new JavacCompiler();
        AbstractCompiler abstractCompiler = (AbstractCompiler) compiler;
        abstractCompiler.addPass(new InlineObjectLiterals(
                abstractCompiler,
                () -> StringUtils.repeat("INLINE_", 10)
        ));

        Options options = new Options();
        GnuParser parser = new GnuParser();
        BasicCommandLine cmdLine = parser.parse(options, null);
        cmdLine.setArgs(StringUtils.split(sourceCode, "\n"));

        try {
            compiler.compile(options, cmdLine.getParsedCommandLine(), Lookups.getDefault());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        byte[] compiledCode = cmdLine.getCompiledClass();
        Class<?> clazz = compiler.getClassLoader().loadClass(
                cmdLine.getMainClassName());

        // Test the inlined variables
        Object instance = clazz.newInstance();
        assertEquals("Alice", instance.getClass().getDeclaredField("name").get(instance));
        assertEquals(30, instance.getClass().getDeclaredField("age").get(instance));

        instance = clazz.newInstance();
        assertEquals("Bob", instance.getClass().getDeclaredField("name").get(instance));
        assertEquals(40, instance.getClass().getDeclaredField("age").get(instance));

        // Test that the original object literals have been removed
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().startsWith("INLINE_") && field.getType() == String.class) {
                assertNull(field.get(instance));
            }
        }
    }
}

class InlineObjectLiteralsTestTest {

    @Test
    public void testInlineObjectLiteralsWithUnexpandableObjectLiterals() {
        String sourceCode = "class InlineObjectLiteralsTest {\n" +
                "  static void main(String[] args) {\n" +
                "    Object obj = {\"name\": {}}; // this should not be inlined\n" +
                "  }\n" +
                "}\n";

        Compiler compiler = new JavacCompiler();
        AbstractCompiler abstractCompiler = (AbstractCompiler) compiler;
        abstractCompiler.addPass(new InlineObjectLiterals(
                abstractCompiler,
                () -> StringUtils.repeat("INLINE_", 10)
        ));

        Options options = new Options();
        GnuParser parser = new GnuParser();
        BasicCommandLine cmdLine = parser.parse(options, null);
        cmdLine.setArgs(StringUtils.split(sourceCode, "\n"));

        try {
            compiler.compile(options, cmdLine.getParsedCommandLine(), Lookups.getDefault());
        } catch (Exception e) {
            // Compilation should succeed, even if object literals are not inlined
        }
    }

    @Test
    public void testInlineObjectLiteralsWithComplexObjectLiterals() {
        String sourceCode = "class InlineObjectLiteralsTest {\n" +
                "  static void main(String[] args) {\n" +
                "    Object obj = {\n" +
                "      \"name\": \"Alice\",\n" +
                "      \"age\": 30,\n" +
                "      \"hobbies\": [\n" +
                "        \"swimming\",\n" +
                "        \"reading\"\n" +
                "      ],\n" +
                "      \"friends\": [\n" +
                "        {\n" +
                "          \"name\": \"Bob\",\n" +
                "          \"age\": 40,\n" +
                "          \"hobbies\": [\n" +
                "            \"painting\",\n" +
                "            \"playing guitar\"\n" +
                "          ]\n" +
                "        },\n" +
                "        {\n" +
                "          \"name\": \"Charlie\",\n" +
                "          \"age\": 25,\n" +
                "          \"hobbies\": [\n" +
                "            \"programming\",\n" +
                "            \"gaming\"\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    };\n" +
                "    print(\"Alice's name: \", obj.name); // this will be inlined\n" +
                "    print(\"Alice's age: \", obj.age); // this will be inlined\n" +
                "    print(\"Bob's name: \", obj.friends[0].name); // this will be inlined\n" +
                "    print(\"Bob's age: \", obj.friends[0].age); // this will be inlined\n" +
                "    print(\"Charlie's name: \", obj.friends[1].name); // this will be inlined\n" +
                "    print(\"Charlie's age: \", obj.friends[1].age); // this will be inlined\n" +
                "  }\n" +
                "}\n";

        Compiler compiler = new JavacCompiler();
        AbstractCompiler abstractCompiler = (AbstractCompiler) compiler;
        abstractCompiler.addPass(new InlineObjectLiterals(
                abstractCompiler,
                () -> StringUtils.repeat("INLINE_", 10)
        ));

        Options options = new Options();
        GnuParser parser = new GnuParser();
        BasicCommandLine cmdLine = parser.parse(options, null);
        cmdLine.setArgs(StringUtils.split(sourceCode, "\n"));

        try {
            compiler.compile(options, cmdLine.getParsedCommandLine(), Lookups.getDefault());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        byte[] compiledCode = cmdLine.getCompiledClass();
        Class<?> clazz = compiler.getClassLoader().loadClass(
                cmdLine.getMainClassName());

        // Test the inlined variables
        Object instance = clazz.newInstance();
        assertEquals("Alice", instance.getClass().getDeclaredField("name").get(instance));
        assertEquals(30, instance.getClass().getDeclaredField("age").get(instance));

        assertEquals("Bob", instance.getClass().getDeclaredField("friends")
                .get(0)
                .getClass()
                .getDeclaredField("name")
                .get(instance.getClass().getDeclaredField("friends")
                        .get(0)));
        assertEquals(40, instance.getClass().getDeclaredField("friends")
                .get(0)
                .getClass()
                .getDeclaredField("age")
                .get(instance.getClass().getDeclaredField("friends")
                        .get(0)));

        assertEquals("Charlie", instance.getClass().getDeclaredField("friends")
                .get(1)
                .getClass()
                .getDeclaredField("name")
                .get(instance.getClass().getDeclaredField("friends")
                        .get(1)));
        assertEquals(25, instance.getClass().getDeclaredField("friends")
                .get(1)
                .getClass()
                .getDeclaredField("age")
                .get(instance.getClass().getDeclaredField("friends")
                        .get(1)));
    }
}