import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;
import com.google.common.base.Sets;

import java.net.URI;
import java.net.URISyntaxException;

public class ProcessCommonJSModulesTest {

  private CompilerInput input;
  private AbstractCompiler compiler;
  private ProcessCommonJSModules processor;

  @org.junit.Before
  public void setUp() {
    input = new CompilerInput("test.js");
    compiler = mock(AbstractCompiler.class);
    processor = new ProcessCommonJSModules(compiler, ".test");
  }

  @org.junit.Test
  public void testProcessCommonJSModules() throws URISyntaxException {
    String source = "var module = {'exports': {'foo': 'bar'}};\n" +
        "require('./test2');\n" +
        "goog.require('test2');\n" +
        "goog.provide('test');\n" +
        "goog.provide('test.foo');\n" +
        "test.foo.bar = 'baz';";

    input.setSource(0, source);
    Node root = input.getRootNode();

    processor.process(input, root);

    JSModule module = processor.getModule();
    assertNull(module);

    Node expected = IR.script(IR.block(
        IR.var(IR.name("test"), IR.objectlit().put("foo", IR.name("test.foo"))),
        IR.var(IR.name("test.foo"), IR.objectlit().put("bar", IR.string("baz"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test.foo")))
    ));

    Node actual = input.getRootNode().getChildAtIndex(0);
    assertEquals(expected.toSource(), actual.toSource());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithRequire() throws URISyntaxException {
    String test1Source = "var module = {'exports': {'foo': 'bar'}};\n" +
        "require('./test2');\n" +
        "goog.require('test2');\n" +
        "goog.provide('test');\n" +
        "goog.provide('test.foo');\n" +
        "test.foo.bar = 'baz';";

    String test2Source = "goog.provide('test2');\n" +
        "module.exports = 'hello';";

    input.setSource(0, test1Source);
    input.setSource(1, test2Source);

    processor.process(input, input.getRootNode());

    JSModule module = processor.getModule();
    assertNull(module);

    Node expected = IR.script(IR.block(
        IR.var(IR.name("test"), IR.objectlit().put("foo", IR.name("test.foo"))),
        IR.var(IR.name("test.foo"), IR.objectlit().put("bar", IR.string("baz"))),
        IR.var(IR.name("test2"), IR.string("hello")),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test.foo")))
    ));

    Node actual = input.getSourceNodeAt(0).getChildAtIndex(0);
    assertEquals(expected.toSource(), actual.toSource());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithRequireRelative()
      throws URISyntaxException {
    String test1Source = "var module = {'exports': {'foo': 'bar'}};\n" +
        "require('./test2');\n" +
        "goog.require('test2');\n" +
        "goog.provide('test');\n" +
        "goog.provide('test.foo');\n" +
        "test.foo.bar = 'baz';";

    String test2Source = "goog.provide('test2');\n" +
        "module.exports = 'hello';";

    String test3Source = "goog.provide('test3');\n" +
        "require('./test2');\n" +
        "goog.require('test');\n" +
        "goog.provide('test.foo.test3');";

    input.setSource(0, test1Source);
    input.setSource(1, test2Source);
    input.setSource(2, test3Source);

    processor.process(input, input.getRootNode());

    JSModule module = processor.getModule();
    assertNull(module);

    Node expected = IR.script(IR.block(
        IR.var(IR.name("test"), IR.objectlit().put("foo", IR.name("test.foo"))),
        IR.var(IR.name("test.foo"), IR.objectlit().put("bar", IR.string("baz"))),
        IR.var(IR.name("test2"), IR.string("hello")),
        IR.var(IR.name("test3"), IR.objectlit().put("foo", IR.name("test.foo.test3"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test.foo"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test.foo.test3")))
    ));

    Node actual = input.getSourceNodeAt(0).getChildAtIndex(0);
    assertEquals(expected.toSource(), actual.toSource());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithRelativeRequire()
      throws URISyntaxException {
    String test1Source = "var module = {'exports': {'foo': 'bar'}};\n" +
        "require('./test2');\n" +
        "goog.require('test2');\n" +
        "goog.provide('test');\n" +
        "goog.provide('test.foo');\n" +
        "test.foo.bar = 'baz';";

    String test2Source = "goog.provide('test2');\n" +
        "module.exports = 'hello';";

    String test3Source = "goog.provide('test3');\n" +
        "require('../test2');\n" +
        "goog.require('test');\n" +
        "goog.provide('test.foo.test3');";

    input.setSource(0, test1Source);
    input.setSource(1, test2Source);
    input.setSource(2, test3Source);

    processor.process(input, input.getRootNode());

    JSModule module = processor.getModule();
    assertNull(module);

    Node expected = IR.script(IR.block(
        IR.var(IR.name("test"), IR.objectlit().put("foo", IR.name("test.foo"))),
        IR.var(IR.name("test.foo"), IR.objectlit().put("bar", IR.string("baz"))),
        IR.var(IR.name("test2"), IR.string("hello")),
        IR.var(IR.name("test3"), IR.objectlit().put("foo", IR.name("test.foo.test3"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test.foo"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test.foo.test3")))
    ));

    Node actual = input.getSourceNodeAt(0).getChildAtIndex(0);
    assertEquals(expected.toSource(), actual.toSource());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithAbsoluteRequire()
      throws URISyntaxException {
    String test1Source = "var module = {'exports': {'foo': 'bar'}};\n" +
        "require('./test2');\n" +
        "goog.require('test2');\n" +
        "goog.provide('test');\n" +
        "goog.provide('test.foo');\n" +
        "test.foo.bar = 'baz';";

    String test2Source = "goog.provide('test2');\n" +
        "module.exports = 'hello';";

    String test3Source = "goog.provide('test3');\n" +
        "require('test2');\n" +
        "goog.require('test');\n" +
        "goog.provide('test.foo.test3');";

    URI test1Uri = new URI("test:test1.js");
    URI test2Uri = new URI("test:test2.js");
    input.setSource(test1Uri);
    input.setSource(test2Uri);

    processor.process(input, input.getRootNode());

    JSModule module = processor.getModule();
    assertNull(module);

    Node expected = IR.script(IR.block(
        IR.var(IR.name("test"), IR.objectlit().put("foo", IR.name("test.foo"))),
        IR.var(IR.name("test.foo"), IR.objectlit().put("bar", IR.string("baz"))),
        IR.var(IR.name("test2"), IR.string("hello")),
        IR.var(IR.name("test3"), IR.objectlit().put("foo", IR.name("test.foo.test3"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test.foo"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test.foo.test3")))
    ));

    Node actual = input.getSourceNodeAt(0).getChildAtIndex(0);
    assertEquals(expected.toSource(), actual.toSource());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithModuleExports()
      throws URISyntaxException {
    String test1Source = "var module = {'exports': {'foo': 'bar'}};\n" +
        "require('./test2');\n" +
        "goog.require('test2');\n" +
        "goog.provide('test');\n" +
        "goog.provide('test.foo');\n" +
        "test.foo.bar = 'baz';\n" +
        "goog.exports = {'myFunction': function() { return 'hello'; }};";

    String test2Source = "goog.provide('test2');\n" +
        "var module = {'exports': {'myFunction': require('./test3').myFunction}};\n" +
        "goog.exports = {'foo': 'hello'};";

    String test3Source = "goog.provide('test3');\n" +
        "exports.myFunction = function() { return 'world'; };";

    input.setSource(0, test1Source);
    input.setSource(1, test2Source);
    input.setSource(2, test3Source);

    processor.process(input, input.getRootNode());

    JSModule module = processor.getModule();
    assertNull(module);

    Node expected = IR.script(IR.block(
        IR.var(IR.name("test"), IR.objectlit().put("foo", IR.name("test.foo"))),
        IR.var(IR.name("test.foo"), IR.objectlit().put("bar", IR.string("baz"))),
        IR.var(IR.name("test2"), IR.objectlit().put("myFunction", IR.name("test.test3.myFunction"))),
        IR.var(IR.name("test3"), IR.objectlit().put("myFunction", IR.name("test3.myFunction"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test.foo"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test2"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test3"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("test2"), "myFunction")),
            IR.call(IR.getprop(IR.name("test3"), "myFunction"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("test"), "myFunction"))),
        IR.exprResult(IR.call(IR.getprop(IR.name("goog"), IR.string("provide")),
            IR.string("test.foo.myFunction")))
    ));

    Node actual = input.getSourceNodeAt(0).getChildAtIndex(0);
    assertEquals(expected.toSource(), actual.toSource());
  }
}