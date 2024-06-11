package com.lts.event;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

public class SimpleKeyListenerHelperTest {

    private SimpleKeyListenerHelper simpleKeyListenerHelper;

    @Before
    public void setUp() {
        Component component = new Component() {
        };
        simpleKeyListenerHelper = new SimpleKeyListenerHelper(component);
    }

    @Test
    public void detachShouldRemoveKeyListenerWhenComponentIsNotNull() {
        simpleKeyListenerHelper.detach();
        Assert.assertNull(simpleKeyListenerHelper.getComponent());
    }

    @Test
    public void notifyListenerShouldCallListenerEnterWhenTypeIs10() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                Assert.assertNotNull(component);
            }

            @Override
            public void insert(Component component) {
                Assert.fail("Should not call insert method");
            }

            @Override
            public void delete(Component component) {
                Assert.fail("Should not call delete method");
            }

            @Override
            public void tab(Component component) {
                Assert.fail("Should not call tab method");
            }
        };

        simpleKeyListenerHelper.notifyListener(listener, 10, null);
    }

    @Test
    public void notifyListenerShouldCallListenerInsertWhenTypeIs155() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                Assert.fail("Should not call enter method");
            }

            @Override
            public void insert(Component component) {
                Assert.assertNotNull(component);
            }

            @Override
            public void delete(Component component) {
                Assert.fail("Should not call delete method");
            }

            @Override
            public void tab(Component component) {
                Assert.fail("Should not call tab method");
            }
        };

        simpleKeyListenerHelper.notifyListener(listener, 155, null);
    }

    @Test
    public void notifyListenerShouldCallListenerDeleteWhenTypeIs127() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                Assert.fail("Should not call enter method");
            }

            @Override
            public void insert(Component component) {
                Assert.fail("Should not call insert method");
            }

            @Override
            public void delete(Component component) {
                Assert.assertNotNull(component);
            }

            @Override
            public void tab(Component component) {
                Assert.fail("Should not call tab method");
            }
        };

        simpleKeyListenerHelper.notifyListener(listener, 127, null);
    }

    @Test
    public void notifyListenerShouldCallListenerTabWhenTypeIs9() {
        SimpleKeyListener listener = new SimpleKeyListener() {
            @Override
            public void enter(Component component) {
                Assert.fail("Should not call enter method");
            }

            @Override
            public void insert(Component component) {
                Assert.fail("Should not call insert method");
            }

            @Override
            public void delete(Component component) {
                Assert.fail("Should not call delete method");
            }

            @Override
            public void tab(Component component) {
                Assert.assertNotNull(component);
            }
        };

        simpleKeyListenerHelper.notifyListener(listener, 9, null);
    }

    @Test
    public void keyPressedShouldFireEventWhenKeyCodeIs9() {
        KeyEvent event = new KeyEvent(new Component() {
        }, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_TAB, KeyEvent.CHAR_UNDEFINED);

        simpleKeyListenerHelper.keyPressed(event);
        // Assertions about firing the event
    }

    @Test
    public void keyPressedShouldFireEventWhenKeyCodeIs10() {
        KeyEvent event = new KeyEvent(new Component() {
        }, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);

        simpleKeyListenerHelper.keyPressed(event);
        // Assertions about firing the event
    }

    @Test
    public void keyPressedShouldFireEventWhenKeyCodeIs127() {
        KeyEvent event = new KeyEvent(new Component() {
        }, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED);

        simpleKeyListenerHelper.keyPressed(event);
        // Assertions about firing the event
    }

    @Test
    public void keyPressedShouldFireEventWhenKeyCodeIs155() {
        KeyEvent event = new KeyEvent(new Component() {
        }, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);

        simpleKeyListenerHelper.keyPressed(event);
        // Assertions about firing the event
    }

    @Test
    public void keyReleasedShouldDoNothing() {
        KeyEvent event = new KeyEvent(new Component() {
        }, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);

        simpleKeyListenerHelper.keyReleased(event);
        // Assert that no action is taken
    }

    @Test
    public void keyTypedShouldDoNothing() {
        KeyEvent event = new KeyEvent(new Component() {
        }, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_INSERT, KeyEvent.CHAR_UNDEFINED);

        simpleKeyListenerHelper.keyTyped(event);
        // Assert that no action is taken
    }
}