package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Maps;

@RunWith(JUnit4.class)
public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testContextualRenamer() {
    MakeDeclaredNamesUnique renewer = new MakeDeclaredNamesUnique();

    String[] inputs = {
        "var a = 1;",
        "var b = 1;",
        "var a = 2;",
        "function f() { var a = 3; }",
        "function f() { var b = 3; }",
        "function f() { var a = 4; }",
        "function f() { var b = 4; }",
        "function g() { function h() { var a = 5; } }",
        "function g() { function h() { var b = 5; } }",
        "function f() { function g() { var a = 6; } }",
        "function f() { function g() { var b = 6; } }"
    };

    for (String input : inputs) {
      Node node = NodeUtil.parseCompilationUnit(input);
      Renamer renamer = new ContextualRenamer();
      renewer.traverse(node, renamer);
      String output = NodeUtil.printCompilationUnit(node);
      String expected = getExpectedOutput(input);
      assertEquals(expected, output);
    }
  }

  @Test
  public void testInlineRenamer() {
    String prefix = "Renamed";
    MakeDeclaredNamesUnique renewer = new MakeDeclaredNamesUnique(() -> prefix, prefix, true);

    String[] inputs = {
        "var a = 1;",
        "var b = 1;",
        "var a = 2;",
        "function f() { var a = 3; }",
        "function f() { var b = 3; }",
        "function f() { var a = 4; }",
        "function f() { var b = 4; }",
        "function g() { function h() { var a = 5; } }",
        "function g() { function h() { var b = 5; } }",
        "function f() { function g() { var a = 6; } }",
        "function f() { function g() { var b = 6; } }"
    };

    for (String input : inputs) {
      Node node = NodeUtil.parseCompilationUnit(input);
      Renamer renamer = new InlineRenamer(() -> prefix, prefix, true);
      renewer.traverse(node, renamer);
      String output = NodeUtil.printCompilationUnit(node);
      String expected = getExpectedOutputWithRenamedPrefix(prefix, input);
      assertEquals(expected, output);
    }
  }

  private String getExpectedOutput(String input) {
    switch (input) {
      case "":
        return "";
      case "var a = 1;":
        return "var a_0 = 1;";
      case "var b = 1;":
        return "var b_1 = 1;";
      case "var a = 2;":
        return "var a_2 = 2;";
      case "function f() { var a = 3; }":
        return "function f() { var a_3 = 3; }";
      case "function f() { var b = 3; }":
        return "function f() { var b_3 = 3; }";
      case "function f() { var a = 4; }":
        return "function f() { var a_4 = 4; }";
      case "function f() { var b = 4; }":
        return "function f() { var b_4 = 4; }";
      case "function g() { function h() { var a = 5; } }":
        return "function g() { function h() { var a_5 = 5; } }";
      case "function g() { function h() { var b = 5; } }":
        return "function g() { function h() { var b_5 = 5; } }";
      case "function f() { function g() { var a = 6; } }":
        return "function f() { function g() { var a_6 = 6; } }";
      case "function f() { function g() { var b = 6; } }":
        return "function f() { function g() { var b_6 = 6; } }";
      default:
        throw new AssertionError("Unknown input: " + input);
    }
  }

  private String getExpectedOutputWithRenamedPrefix(String prefix, String input) {
    String expected = getExpectedOutput(input);
    // Replace the numbers with the prefix and incremented number
    String[] lines = expected.split("\\r?\\n");
    for (int i = 0; i < lines.length; ++i) {
      lines[i] = lines[i].replaceAll("_\\d+", prefix + "_$1");
    }
    return String.join("\n", lines);
  }
}