package dsachat.client.gui;

import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public class InternalChatFrameTest {

    private InternalChatFrame internalChatFrame;

    @Before
    public void setUp() {
        internalChatFrame = new InternalChatFrame(new MainFrame());
    }

    @Test
    public void testAddText() {
        internalChatFrame.addText("This is a test");
        assertEquals("This is a test\n", internalChatFrame.getChatlog());
    }

    @Test
    public void testAddUser() {
        internalChatFrame.addUser("John");
        String[] expectedUserlist = {"John"};
        assertArrayEquals(expectedUserlist, internalChatFrame.getUserlist());
    }

    @Test
    public void testDeleteUser() {
        internalChatFrame.addUser("John");
        internalChatFrame.deleteUser("John");
        String[] expectedUserlist = {};
        assertArrayEquals(expectedUserlist, internalChatFrame.getUserlist());
    }
}