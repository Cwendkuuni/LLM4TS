import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import geo.google.datamodel.AddressDetails;
import geo.google.datamodel.AdministrativeArea;
import geo.google.datamodel.GeoAddress;
import geo.google.datamodel.GeoException;
import geo.google.datamodel.GeoUsAddress;
import geo.google.datamodel.Locality;
import geo.google.datamodel.PostalCode;
import geo.google.datamodel.ThoroughfareNameType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddressToUsAddressFunctorTest {
    
    private AddressToUsAddressFunctor functor;
    
    @Before
    public void setUp() {
        functor = AddressToUsAddressFunctor.getInstance();
    }
    
    @Test
    public void testExecute_withValidGeoAddress_shouldReturnGeoUsAddressWithCorrectValues() {
        // Arrange
        GeoAddress geoAddress = createGeoAddress();
        
        // Act
        GeoUsAddress actual = functor.execute(geoAddress);
        
        // Assert
        assertEquals("Country", "US", actual.getCountry());
        assertEquals("State", "California", actual.getState());
        assertEquals("County", "Los Angeles", actual.getCounty());
        assertEquals("City", "Los Angeles", actual.getCity());
        assertEquals("Postal Code", "90001", actual.getPostalCode());
        assertEquals("Address Line 1", "123 Main St", actual.getAddressLine1());
        assertEquals("Address Line 2", "Apt 4B", actual.getAddressLine2());
        assertEquals("Accuracy", 95.0, actual.getAccuracy(), 0.01);
    }
    
    @Test
    public void testExecute_withNullGeoAddress_shouldReturnEmptyGeoUsAddress() {
        // Arrange
        GeoAddress geoAddress = null;
        
        // Act
        GeoUsAddress actual = functor.execute(geoAddress);
        
        // Assert
        assertEquals("Country", "", actual.getCountry());
        assertEquals("State", "", actual.getState());
        assertEquals("County", "", actual.getCounty());
        assertEquals("City", "", actual.getCity());
        assertEquals("Postal Code", "", actual.getPostalCode());
        assertEquals("Address Line 1", "", actual.getAddressLine1());
        assertEquals("Address Line 2", "", actual.getAddressLine2());
        assertEquals("Accuracy", 0.0, actual.getAccuracy(), 0.01);
    }
    
    private GeoAddress createGeoAddress() {
        GeoAddress geoAddress = new GeoAddress();
        
        AddressDetails.Country country = new AddressDetails.Country();
        AddressDetails.Country.CountryNameCode countryName = new AddressDetails.Country.CountryNameCode();
        countryName.setContent("US");
        country.setCountryNameCode(Collections.singletonList(countryName));
        geoAddress.setAddressDetails(new AddressDetails(country));
        
        AdministrativeArea adminArea = new AdministrativeArea();
        AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
        adminAreaName.setContent("California");
        adminArea.setAdministrativeAreaName(Collections.singletonList(adminAreaName));
        geoAddress.getAddressDetails().getCountry().setAdministrativeArea(adminArea);
        
        AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
        AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAdminAreaName = new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
        subAdminAreaName.setContent("Los Angeles");
        subAdminArea.setSubAdministrativeAreaName(Collections.singletonList(subAdminAreaName));
        geoAddress.getAddressDetails().getCountry().getAdministrativeArea().setSubAdministrativeArea(subAdminArea);
        
        Locality locality = new Locality();
        Locality.LocalityName localityName = new Locality.LocalityName();
        localityName.setContent("Los Angeles");
        locality.setLocalityName(Collections.singletonList(localityName));
        PostalCode postalCode = new PostalCode();
        PostalCode.PostalCodeNumber postalCodeNumber = new PostalCode.PostalCodeNumber();
        postalCodeNumber.setContent("90001");
        postalCode.setPostalCodeNumber(Collections.singletonList(postalCodeNumber));
        locality.setPostalCode(postalCode);
        ThoroughfareNameType thoroughfare = new ThoroughfareNameType();
        ThoroughfareNameType thoroughfareName = new ThoroughfareNameType();
        thoroughfareName.setContent("123 Main St");
        thoroughfare.setThoroughfareName(Arrays.asList(thoroughfareName, new ThoroughfareNameType()));
        locality.setThoroughfare(thoroughfare);
        geoAddress.getAddressDetails().getCountry().getAdministrativeArea().setLocality(locality);
        
        geoAddress.getCoordinate().setLatitude(37.7749);
        geoAddress.getCoordinate().setLongitude(-122.4194);
        geoAddress.setAccuracy(95.0);
        
        return geoAddress;
    }
}