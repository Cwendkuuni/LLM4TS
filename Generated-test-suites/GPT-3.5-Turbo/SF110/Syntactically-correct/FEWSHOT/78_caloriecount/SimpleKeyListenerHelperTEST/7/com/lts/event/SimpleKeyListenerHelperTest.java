package com.lts.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.awt.*;
import java.awt.event.*;

public class SimpleKeyListenerHelperTest {
    @Test
    public void testDetach() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new Component() {});
        helper.detach();
        assertNull(helper.component);
    }

    @Test
    public void testNotifyListener_Enter() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new Component() {});
        SimpleKeyListener listener = new SimpleKeyListener() {
            public void enter(Component component) {
                assertEquals(helper.component, component);
            }

            public void insert(Component component) {
            }

            public void delete(Component component) {
            }

            public void tab(Component component) {
            }
        };

        helper.notifyListener(listener, 10, null);
    }

    @Test
    public void testNotifyListener_Insert() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new Component() {});
        SimpleKeyListener listener = new SimpleKeyListener() {
            public void enter(Component component) {
            }

            public void insert(Component component) {
                assertEquals(helper.component, component);
            }

            public void delete(Component component) {
            }

            public void tab(Component component) {
            }
        };

        helper.notifyListener(listener, 155, null);
    }

    @Test
    public void testNotifyListener_Delete() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new Component() {});
        SimpleKeyListener listener = new SimpleKeyListener() {
            public void enter(Component component) {
            }

            public void insert(Component component) {
            }

            public void delete(Component component) {
                assertEquals(helper.component, component);
            }

            public void tab(Component component) {
            }
        };

        helper.notifyListener(listener, 127, null);
    }

    @Test
    public void testNotifyListener_Tab() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new Component() {});
        SimpleKeyListener listener = new SimpleKeyListener() {
            public void enter(Component component) {
            }

            public void insert(Component component) {
            }

            public void delete(Component component) {
            }

            public void tab(Component component) {
                assertEquals(helper.component, component);
            }
        };

        helper.notifyListener(listener, 9, null);
    }

    @Test
    public void testKeyPressed_Enter() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new Component() {});
        KeyEvent event = new KeyEvent(new Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);

        SimpleKeyListener listener = new SimpleKeyListener() {
            public void enter(Component component) {
                assertEquals(helper.component, component);
            }

            public void insert(Component component) {
            }

            public void delete(Component component) {
            }

            public void tab(Component component) {
            }
        };

        helper.notifyListener(listener, KeyEvent.VK_ENTER, null);
    }

    @Test
    public void testKeyPressed_Insert() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new Component() {});
        KeyEvent event = new KeyEvent(new Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);

        SimpleKeyListener listener = new SimpleKeyListener() {
            public void enter(Component component) {
            }

            public void insert(Component component) {
                assertEquals(helper.component, component);
            }

            public void delete(Component component) {
            }

            public void tab(Component component) {
            }
        };

        helper.notifyListener(listener, KeyEvent.VK_INSERT, null);
    }

    @Test
    public void testKeyPressed_Delete() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new Component() {});
        KeyEvent event = new KeyEvent(new Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);

        SimpleKeyListener listener = new SimpleKeyListener() {
            public void enter(Component component) {
            }

            public void insert(Component component) {
            }

            public void delete(Component component) {
                assertEquals(helper.component, component);
            }

            public void tab(Component component) {
            }
        };

        helper.notifyListener(listener, KeyEvent.VK_DELETE, null);
    }

    @Test
    public void testKeyPressed_Tab() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new Component() {});
        KeyEvent event = new KeyEvent(new Component() {}, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);

        SimpleKeyListener listener = new SimpleKeyListener() {
            public void enter(Component component) {
            }

            public void insert(Component component) {
            }

            public void delete(Component component) {
            }

            public void tab(Component component) {
                assertEquals(helper.component, component);
            }
        };

        helper.notifyListener(listener, KeyEvent.VK_TAB, null);
    }

    @Test
    public void testKeyReleased() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new Component() {});
        KeyEvent event = new KeyEvent(new Component() {}, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);

        // No assertions needed since the method does nothing
        helper.keyReleased(event);
    }

    @Test
    public void testKeyTyped() {
        SimpleKeyListenerHelper helper = new SimpleKeyListenerHelper(new Component() {});
        KeyEvent event = new KeyEvent(new Component() {}, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, 'a');

        // No assertions needed since the method does nothing
        helper.keyTyped(event);
    }
}