package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

public class LeafNodeTest {

    @Test
    public void testHasAttributes() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("id", "testId");
            }
        };

        Assert.assertTrue(leafNode.hasAttributes());
    }

    @Test
    public void testHasAttributes_NullValue() {
        LeafNode leafNode = new LeafNode();
        Assert.assertFalse(leafNode.hasAttributes());
    }

    @Test
    public void testCoreValue_EmptyNodeName() {
        LeafNode leafNode = new LeafNode() {
            {
                value = "";
            }
        };

        Assert.assertEquals("", leafNode.coreValue());
    }

    @Test
    public void testCoreValue_StringValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = "Test Value";
            }
        };

        Assert.assertEquals("Test Value", leafNode.coreValue());
    }

    @Test
    public void testCoreValue_AttributesValue() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };

        Assert.assertNull(leafNode.coreValue());
    }

    @Test
    public void testAttr_ValidKey() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("id", "testId");
            }
        };

        Assert.assertEquals("testId", leafNode.attr("id"));
    }

    @Test
    public void testAttr_InvalidKey() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };

        Assert.assertEquals(LeafNode.EmptyString, leafNode.attr("invalidKey"));
    }

    @Test(expected = NullPointerException.class)
    public void testAttr_NullKey() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };

        leafNode.attr(null, "value");
    }

    @Test
    public void testAttr_SetAndGetAttribute() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };

        leafNode.attr("id", "testId");
        Assert.assertEquals("testId", leafNode.attr("id"));
    }

    @Test
    public void testAttr_ValueAlreadyPresent() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("id", "testId");
            }
        };

        leafNode.attr("id", "testId2");
        Assert.assertEquals("testId2", leafNode.attr("id"));
    }

    @Test
    public void testHasAttr_ValidKey() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("id", "testId");
            }
        };

        Assert.assertTrue(leafNode.hasAttr("id"));
    }

    @Test
    public void testHasAttr_InvalidKey() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };

        Assert.assertFalse(leafNode.hasAttr("invalidKey"));
    }

    @Test
    public void testRemoveAttr_ValidKey() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("id", "testId");
            }
        };

        leafNode.removeAttr("id");
        Assert.assertFalse(leafNode.hasAttr("id"));
    }

    @Test
    public void testRemoveAttr_InvalidKey() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
            }
        };

        leafNode.removeAttr("invalidKey");
        Assert.assertTrue(leafNode.hasAttr("invalidKey"));
    }

    @Test
    public void testAbsUrl_ValidKey() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("href", "http://www.test.com");
            }
        };

        Assert.assertEquals("http://www.test.com", leafNode.absUrl("href"));
    }

    @Test
    public void testAbsUrl_EmptyBaseUri() {
        LeafNode leafNode = new LeafNode() {
            {
                value = new Attributes();
                putAttribute("href", "http://www.test.com");
            }
        };

        Assert.assertEquals("http://www.test.com", leafNode.absUrl("href"));
    }

    @Test
    public void testBaseUri_NullParent() {
        LeafNode leafNode = new LeafNode();

        Assert.assertEquals("", leafNode.baseUri());
    }

    @Test
    public void testBaseUri_HasParent() {
        LeafNode parentNode = new LeafNode() {
            {
                value = "http://www.parent.com";
            }
        };

        LeafNode leafNode = new LeafNode() {
            {
                parent = parentNode;
            }
        };

        Assert.assertEquals("http://www.parent.com", leafNode.baseUri());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChildNodeSize() {
        LeafNode leafNode = new LeafNode();

        leafNode.childNodeSize();
    }
}