import geo.google.datamodel.*;
import geo.google.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import org.apache.commons.collections.*;
import org.junit.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute_validAddress_returnsGeoUsAddress() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        GeoUsAddress expected = new GeoUsAddress();
        
        // Set up address details
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("US");
        country.setCountryNameCode(Arrays.asList(countryNameCode));
        
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName administrativeAreaName = new AdministrativeArea.AdministrativeAreaName();
        administrativeAreaName.setContent("California");
        adminArea.setAdministrativeAreaName(Arrays.asList(administrativeAreaName));
        
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("Los Angeles");
        locality.setLocalityName(Arrays.asList(localityName));
        
        PostalCode postalCode = new PostalCode();
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("90001");
        postalCode.setPostalCodeNumber(Arrays.asList(postalCodeNumber));
        
        Thoroughfare thoroughfare = new Thoroughfare();
        Thoroughfare.ThoroughfareName thoroughfareName1 = new Thoroughfare.ThoroughfareName();
        thoroughfareName1.setContent("Main St");
        Thoroughfare.ThoroughfareName thoroughfareName2 = new Thoroughfare.ThoroughfareName();
        thoroughfareName2.setContent("Apt 10");
        thoroughfare.setThoroughfareName(Arrays.asList(thoroughfareName1, thoroughfareName2));
        
        locality.setPostalCode(postalCode);
        locality.setThoroughfare(thoroughfare);
        
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        addressDetails.setCountry(country);
        addr.setAddressDetails(addressDetails);
        
        expected.setCountry("US");
        expected.setState("California");
        expected.setCity("Los Angeles");
        expected.setPostalCode("90001");
        expected.setAddressLine1("Main St");
        expected.setAddressLine2("Apt 10");
        
        // Act
        GeoUsAddress result = functor.execute(addr);
        
        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testExecute_nullAddress_returnsNull() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        GeoAddress addr = null;
        
        // Act
        GeoUsAddress result = functor.execute(addr);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testExecute_invalidAddressDetails_returnsEmptyGeoUsAddress() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        GeoAddress addr = new GeoAddress();
        
        // Set up invalid address details
        AddressDetails.Country country = new AddressDetails.Country();
        
        addr.setAddressDetails(addressDetails);
        
        GeoUsAddress expected = new GeoUsAddress();
        
        // Act
        GeoUsAddress result = functor.execute(addr);
        
        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testExecute_invalidCountryName_returnsEmptyGeoUsAddress() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        
        // Set up invalid country name
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("");
        country.setCountryNameCode(Arrays.asList(countryNameCode));
        
        addressDetails.setCountry(country);
        addr.setAddressDetails(addressDetails);
        
        GeoUsAddress expected = new GeoUsAddress();
        
        // Act
        GeoUsAddress result = functor.execute(addr);
        
        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testExecute_noAdministrativeArea_returnsEmptyGeoUsAddress() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        
        // Set up no administrative area
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("US");
        country.setCountryNameCode(Arrays.asList(countryNameCode));
        
        addressDetails.setCountry(country);
        addr.setAddressDetails(addressDetails);
        
        GeoUsAddress expected = new GeoUsAddress();
        
        // Act
        GeoUsAddress result = functor.execute(addr);
        
        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testExecute_invalidLocality_returnsEmptyGeoUsAddress() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        GeoUsAddress expected = new GeoUsAddress();
        
        // Set up invalid locality
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("US");
        country.setCountryNameCode(Arrays.asList(countryNameCode));
        
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName administrativeAreaName = new AdministrativeArea.AdministrativeAreaName();
        administrativeAreaName.setContent("California");
        adminArea.setAdministrativeAreaName(Arrays.asList(administrativeAreaName));
        country.setAdministrativeArea(adminArea);
        
        addressDetails.setCountry(country);
        addr.setAddressDetails(addressDetails);
        
        expected.setCountry("US");
        expected.setState("California");
        
        // Act
        GeoUsAddress result = functor.execute(addr);
        
        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testExecute_invalidPostalCode_returnsEmptyGeoUsAddress() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        GeoUsAddress expected = new GeoUsAddress();
        
        // Set up invalid postal code
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("US");
        country.setCountryNameCode(Arrays.asList(countryNameCode));
        
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName administrativeAreaName = new AdministrativeArea.AdministrativeAreaName();
        administrativeAreaName.setContent("California");
        adminArea.setAdministrativeAreaName(Arrays.asList(administrativeAreaName));
        country.setAdministrativeArea(adminArea);
        
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("Los Angeles");
        locality.setLocalityName(Arrays.asList(localityName));
        
        PostalCode postalCode = new PostalCode();
        postalCode.setPostalCodeNumber(Collections.emptyList());
        locality.setPostalCode(postalCode);
        
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        addressDetails.setCountry(country);
        addr.setAddressDetails(addressDetails);
        
        expected.setCountry("US");
        expected.setState("California");
        expected.setCity("Los Angeles");
        
        // Act
        GeoUsAddress result = functor.execute(addr);
        
        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testExecute_invalidThoroughfare_returnsGeoUsAddressWithoutAddressLine() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        GeoUsAddress expected = new GeoUsAddress();
        
        // Set up invalid thoroughfare
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryNameCode = new AddressDetails.Country.CountryNameCode();
        countryNameCode.setContent("US");
        country.setCountryNameCode(Arrays.asList(countryNameCode));
        
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName administrativeAreaName = new AdministrativeArea.AdministrativeAreaName();
        administrativeAreaName.setContent("California");
        adminArea.setAdministrativeAreaName(Arrays.asList(administrativeAreaName));
        country.setAdministrativeArea(adminArea);
        
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("Los Angeles");
        locality.setLocalityName(Arrays.asList(localityName));
        
        PostalCode postalCode = new PostalCode();
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("90001");
        postalCode.setPostalCodeNumber(Arrays.asList(postalCodeNumber));
        
        locality.setPostalCode(postalCode);
        
        adminArea.setLocality(locality);
        country.setAdministrativeArea(adminArea);
        addressDetails.setCountry(country);
        addr.setAddressDetails(addressDetails);
        
        expected.setCountry("US");
        expected.setState("California");
        expected.setCity("Los Angeles");
        expected.setPostalCode("90001");
        
        // Act
        GeoUsAddress result = functor.execute(addr);
        
        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testExecute_coordinateNotNull_returnsGeoUsAddressWithClonedCoordinate() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        GeoUsAddress expected = new GeoUsAddress();
        
        // Set up coordinate
        Coordinate coordinate = new Coordinate();
        coordinate.setLatitude(37.7749);
        coordinate.setLongitude(-122.4194);
        
        addr.setCoordinate(coordinate);
        
        // Act
        GeoUsAddress result = functor.execute(addr);
        
        // Assert
        assertEquals(coordinate, result.getCoordinate());
        assertNotSame(coordinate, result.getCoordinate());
    }

    @Test
    public void testExecute_accuracySet_returnsGeoUsAddressWithSameAccuracy() {
        // Arrange
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        GeoUsAddress expected = new GeoUsAddress();
        
        // Set up accuracy
        int accuracy = 90;
        addr.setAccuracy(accuracy);
        
        expected.setAccuracy(accuracy);
        
        // Act
        GeoUsAddress result = functor.execute(addr);
        
        // Assert
        assertEquals(expected, result);
    }

}