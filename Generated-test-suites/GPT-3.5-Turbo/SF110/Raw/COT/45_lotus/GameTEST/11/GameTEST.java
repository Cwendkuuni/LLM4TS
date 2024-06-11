
import org.junit.Test;
import static org.junit.Assert.*;
import lotus.core.*;
import lotus.core.stack.*;
import lotus.core.interfaces.*;
import lotus.core.phases.*;
import lotus.core.card.*;
import java.util.*;

public class GameTest {

    @Test
    public void testGetPlayingPlayer() {
        // Arrange
        // ...
        // Act
        // ...
        // Assert
        // ...
    }

    @Test
    public void testGetNonPlayingPlayer() {
        // Arrange
        // ...
        // Act
        // ...
        // Assert
        // ...
    }

    @Test
    public void testGetOtherPlayer() {
        // Arrange
        // ...
        // Act
        // ...
        // Assert
        // ...
    }

    @Test
    public void testInit() {
        // Arrange
        // ...
        // Act
        // ...
        // Assert
        // ...
    }

    @Test
    public void testGivePriorityToCurrentPlayer() {
        // Arrange
        // ...
        // Act
        // ...
        // Assert
        // ...
    }

    // Additional test cases for edge cases and exceptions

    @Test
    public void testGetPlayingPlayer_P1() {
        // Arrange
        Game.playingPlayer = 1;
        Player expected = Game.player1;

        // Act
        Player actual = Game.getPlayingPlayer();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetPlayingPlayer_P2() {
        // Arrange
        Game.playingPlayer = 2;
        Player expected = Game.player2;

        // Act
        Player actual = Game.getPlayingPlayer();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetNonPlayingPlayer_P1() {
        // Arrange
        Game.playingPlayer = 1;
        Player expected = Game.player2;

        // Act
        Player actual = Game.getNonPlayingPlayer();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    public void testGetNonPlayingPlayer_P2() {
        // Arrange
        Game.playingPlayer = 2;
        Player expected = Game.player1;

        // Act
        Player actual = Game.getNonPlayingPlayer();

        // Assert
        assertEquals(expected, actual);
    }

    // ... Additional test cases for other methods

}
