package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testContextualRenamer() {
    ContextualRenamer renamer = new ContextualRenamer();

    // Test empty name
    assertEquals("", renamer.getReplacementName(""));

    // Test global name
    assertEquals("var1", renamer.getReplacementName("var1"));

    // Test local name with no conflicts
    ContextualRenamer renamer2 = new ContextualRenamer();
    renamer2.addDeclaredName("var1");
    assertEquals("var2", renamer2.getReplacementName("var1"));

    // Test local name with conflicts
    ContextualRenamer renamer3 = new ContextualRenamer();
    renamer3.addDeclaredName("var1");
    renamer3.addDeclaredName("var1");
    assertEquals("var1$$0", renamer3.getReplacementName("var1"));

    // Test local name with multiple conflicts
    ContextualRenamer renamer4 = new ContextualRenamer();
    renamer4.addDeclaredName("var1");
    renamer4.addDeclaredName("var1");
    renamer4.addDeclaredName("var1");
    assertEquals("var1$$0", renamer4.getReplacementName("var1"));
    assertEquals("var1$$1", renamer4.getReplacementName("var1$$0"));
    assertEquals("var1$$2", renamer4.getReplacementName("var1$$1"));

    // Test local name with conflicts and custom prefix
    ContextualRenamer renamer5 = new ContextualRenamer();
    renamer5.addDeclaredName("var1");
    renamer5.addDeclaredName("var1");
    renamer5.addDeclaredName("var1");
    assertEquals("prefixVar1$$0", renamer5.getReplacementName("var1"));
    assertEquals("prefixVar1$$1", renamer5.getReplacementName("prefixVar1$$0"));
    assertEquals("prefixVar1$$2", renamer5.getReplacementName("prefixVar1$$1"));
  }

  @Test
  public void testInlineRenamer() {
    Supplier<String> uniqueIdSupplier = () -> "suffix";
    String idPrefix = "prefix";
    boolean removeConstness = false;
    InlineRenamer renamer = new InlineRenamer(uniqueIdSupplier, idPrefix, removeConstness);

    // Test empty name
    assertEquals("", renamer.getReplacementName(""));

    // Test global name
    assertEquals("var1", renamer.getReplacementName("var1"));

    // Test local name with no conflicts
    renamer.addDeclaredName("var1");
    assertEquals("prefixVar1prefixsuffix", renamer.getReplacementName("var1"));

    // Test local name with conflicts
    renamer.addDeclaredName("var1");
    renamer.addDeclaredName("var1");
    assertEquals("prefixVar1prefixsuffix", renamer.getReplacementName("var1"));

    // Test local name with multiple conflicts
    renamer.addDeclaredName("var1");
    renamer.addDeclaredName("var1");
    renamer.addDeclaredName("var1");
    assertEquals("prefixVar1prefixsuffix", renamer.getReplacementName("var1"));
    assertEquals("prefixVar1prefixsuffix$$1", renamer.getReplacementName("prefixVar1prefixsuffix"));
    assertEquals("prefixVar1prefixsuffix$$2", renamer.getReplacementName("prefixVar1prefixsuffix$$1"));

    // Test local name with conflicts and custom prefix and remove constness
    renamer = new InlineRenamer(uniqueIdSupplier, idPrefix, true);
    renamer.addDeclaredName("var1");
    renamer.addDeclaredName("var1");
    renamer.addDeclaredName("var1");
    assertEquals("prefixVar1", renamer.getReplacementName("var1"));
    assertEquals("prefixVar1$$1", renamer.getReplacementName("prefixVar1"));
    assertEquals("prefixVar1$$2", renamer.getReplacementName("prefixVar1$$1"));
  }

}