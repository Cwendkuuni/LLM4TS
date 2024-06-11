package ioproject.server.network;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import ioproject.common.network.Client;

public class ClientGroupTest {
  
  private ClientGroup parentGroup;
  private ClientGroup subGroup;
  private ClientGroup emptyGroup;

  @Before
  public void setUp() {
    emptyGroup = new ClientGroup(null);
    parentGroup = new ClientGroup(null);
    subGroup = parentGroup.createSubGroup();
  }

  @Test
  public void testParentGroup() {
    assertNull(emptyGroup.parentGroup());
    assertEquals(parentGroup, subGroup.parentGroup());
  }

  @Test
  public void testNotifyMessageSent() {
    Client client = new Client();
    Object message = new Object();

    assertFalse(emptyGroup.notifyMessageSent(client, message));
    assertFalse(emptyGroup.notifyMessageSent(client, message));
    assertFalse(parentGroup.notifyMessageSent(client, message));
    assertFalse(subGroup.notifyMessageSent(client, message));
  }

  @Test
  public void testNotifyMessageReceived() {
    Client client = new Client();
    Object message = new Object();

    assertFalse(emptyGroup.notifyMessageReceived(client, message));
    assertFalse(emptyGroup.notifyMessageReceived(client, message));
    assertFalse(parentGroup.notifyMessageReceived(client, message));
    assertFalse(subGroup.notifyMessageReceived(client, message));
  }

  @Test
  public void testNotifyExceptionCaught() {
    Client client = new Client();
    Throwable cause = new Throwable();

    assertFalse(emptyGroup.notifyExceptionCaught(client, cause));
    assertFalse(emptyGroup.notifyExceptionCaught(client, cause));
    assertFalse(parentGroup.notifyExceptionCaught(client, cause));
    assertFalse(subGroup.notifyExceptionCaught(client, cause));
  }

  @Test
  public void testIterator() {
    assertNotNull(emptyGroup.iterator());
    assertNotNull(parentGroup.iterator());
    assertNotNull(subGroup.iterator());
  }

  @Test
  public void testContains() {
    Client client = new Client();

    assertFalse(emptyGroup.contains(client));
    assertFalse(parentGroup.contains(client));
    assertFalse(subGroup.contains(client));
  }

  @Test
  public void testSize() {
    assertEquals(0, emptyGroup.size());
    assertEquals(0, parentGroup.size());
    assertEquals(0, subGroup.size());
  }

  @Test
  public void testAdd() {
    Client client = new Client();

    assertTrue(emptyGroup.add(client));
    assertFalse(parentGroup.add(client));
    assertFalse(subGroup.add(client));
  }

  @Test
  public void testRemove() {
    Client client = new Client();

    assertFalse(emptyGroup.remove(client));
    assertFalse(parentGroup.remove(client));
    assertFalse(subGroup.remove(client));
  }

  @Test
  public void testCreateSubGroup() {
    ClientGroup subGroup = parentGroup.createSubGroup();

    assertNotNull(subGroup);
    assertEquals(parentGroup, subGroup.parentGroup());
  }

  @Test
  public void testAddClientHandler() {
    ClientHandler handler = new ClientHandler();

    emptyGroup.addClientHandler(handler);
    parentGroup.addClientHandler(handler);
    subGroup.addClientHandler(handler);
  }

}