package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TreeBuilderTest {

 @Test
 public void testProcessStartTag() {
 TreeBuilder treeBuilder = new TreeBuilder();
 String name = "html";
 Attributes attrs = new Attributes();
 boolean result = treeBuilder.processStartTag(name, attrs);
 assertTrue(result);
 }

 @Test
 public void testProcessStartTagWithInvalidTagName() {
 TreeBuilder treeBuilder = new TreeBuilder();
 String name = "";
 Attributes attrs = new Attributes();
 boolean result = treeBuilder.processStartTag(name, attrs);
 assertFalse(result);
 }

 @Test
 public void testProcessStartTagWithEmptyTagName() {
 TreeBuilder treeBuilder = new TreeBuilder();
 String name = "";
 Attributes attrs = new Attributes();
 boolean result = treeBuilder.processStartTag(name, attrs);
 assertFalse(result);
 }

 @Test(expected = NullPointerException.class)
 public void testProcessStartTagWithNullAttributes() {
 TreeBuilder treeBuilder = new TreeBuilder();
 String name = "html";
 Attributes attrs = null;
 treeBuilder.processStartTag(name, attrs);
 }
}