
package org.jsoup.nodes;

import org.jsoup.helper.Validate;

import java.util.List;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "TestNode";
            }
        };

        // Test when attributes have not been set
        assert !leafNode.hasAttributes();

        // Test when attributes have been set
        leafNode.attr("attr1", "value1");
        assert leafNode.hasAttributes();
    }

    @Test
    public void testAttributes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "TestNode";
            }
        };

        // Test when attributes have not been set
        assert leafNode.attributes() != null;

        // Test when attributes have been set
        leafNode.attr("attr1", "value1");
        assert leafNode.attributes() != null;
    }

    @Test
    public void testCoreValue() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "TestNode";
            }
        };

        // Test when core value has not been set
        assert leafNode.coreValue() == null;

        // Test when core value has been set
        leafNode.coreValue("value1");
        assert leafNode.coreValue().equals("value1");
    }

    @Test
    public void testAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "TestNode";
            }
        };

        // Test when attribute is present
        leafNode.attr("attr1", "value1");
        assert leafNode.attr("attr1").equals("value1");

        // Test when attribute is not present
        assert leafNode.attr("attr2").equals("");

        // Test when attribute is the same as node name
        assert leafNode.attr("TestNode").equals("value1");
    }

    @Test
    public void testHasAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "TestNode";
            }
        };

        // Test when attribute is present
        leafNode.attr("attr1", "value1");
        assert leafNode.hasAttr("attr1");

        // Test when attribute is not present
        assert !leafNode.hasAttr("attr2");
    }

    @Test
    public void testRemoveAttr() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "TestNode";
            }
        };

        // Test when attribute is present
        leafNode.attr("attr1", "value1");
        leafNode.removeAttr("attr1");
        assert !leafNode.hasAttr("attr1");

        // Test when attribute is not present
        leafNode.removeAttr("attr2");
        assert !leafNode.hasAttr("attr2");
    }

    @Test
    public void testAbsUrl() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "TestNode";
            }
        };

        // Test when attribute is present
        leafNode.attr("href", "/page");
        assert leafNode.absUrl("href").equals("/page");

        // Test when attribute is not present
        assert leafNode.absUrl("src").equals("");

    }

    @Test
    public void testBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "TestNode";
            }
        };

        // Test when parent is present
        Node parentNode = new Node() {
            @Override
            public String nodeName() {
                return "ParentNode";
            }
        };
        parentNode.childNode(leafNode);
        assert leafNode.baseUri().equals("");

        // Test when parent is not present
        assert leafNode.baseUri().equals("");
    }

    @Test
    public void testDoSetBaseUri() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "TestNode";
            }
        };

        // Test with any baseUri
        leafNode.doSetBaseUri("http://example.com");
        // No assertion needed as method is a noop.
    }

    @Test
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "TestNode";
            }
        };

        assert leafNode.childNodeSize() == 0;
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEnsureChildNodes() {
        LeafNode leafNode = new LeafNode() {
            @Override
            public String nodeName() {
                return "TestNode";
            }
        };

        leafNode.ensureChildNodes(); // Should throw an exception
    }
}

