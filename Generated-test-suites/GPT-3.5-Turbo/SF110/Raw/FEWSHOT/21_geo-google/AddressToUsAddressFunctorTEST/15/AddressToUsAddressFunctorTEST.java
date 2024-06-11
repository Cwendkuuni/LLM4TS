package geo.google.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import geo.google.datamodel.GeoAddress;
import geo.google.datamodel.GeoUsAddress;
import geo.google.GeoException;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails;

public class AddressToUsAddressFunctorTest {
    private AddressToUsAddressFunctor addressToUsAddressFunctor;
    private GeoAddress geoAddress;
    private AddressDetails.Country country;
    private AddressDetails.Country.CountryNameCode countryCode;
    private AddressDetails.Country.AdministrativeArea adminArea;
    private AddressDetails.Country.AdministrativeArea.AdministrativeAreaName adminAreaName;
    private AddressDetails.Country.AdministrativeArea.SubAdministrativeArea subAdminArea;
    private AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAdminAreaName;
    private AddressDetails.Country.AdministrativeArea.Locality locality;
    private AddressDetails.Country.AdministrativeArea.Locality.LocalityName localityName;
    private AddressDetails.Country.AdministrativeArea.Locality.PostalCode postalCode;
    private AddressDetails.Country.AdministrativeArea.Locality.PostalCode.PostalCodeNumber postalCodeNumber;
    private AddressDetails.Country.AdministrativeArea.Locality.Thoroughfare thoroughfare;
    private AddressDetails.Country.AdministrativeArea.Locality.Thoroughfare.ThoroughfareName thoroughfareName;

    @Before
    public void setup() {
        addressToUsAddressFunctor = AddressToUsAddressFunctor.getInstance();

        geoAddress = mock(GeoAddress.class);

        country = mock(AddressDetails.Country.class);
        countryCode = mock(AddressDetails.Country.CountryNameCode.class);

        adminArea = mock(AddressDetails.Country.AdministrativeArea.class);
        adminAreaName = mock(AddressDetails.Country.AdministrativeArea.AdministrativeAreaName.class);
        subAdminArea = mock(AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.class);
        subAdminAreaName = mock(AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName.class);

        locality = mock(AddressDetails.Country.AdministrativeArea.Locality.class);
        localityName = mock(AddressDetails.Country.AdministrativeArea.Locality.LocalityName.class);
        postalCode = mock(AddressDetails.Country.AdministrativeArea.Locality.PostalCode.class);
        postalCodeNumber = mock(AddressDetails.Country.AdministrativeArea.Locality.PostalCode.PostalCodeNumber.class);
        thoroughfare = mock(AddressDetails.Country.AdministrativeArea.Locality.Thoroughfare.class);
        thoroughfareName = mock(AddressDetails.Country.AdministrativeArea.Locality.Thoroughfare.ThoroughfareName.class);

        when(geoAddress.getAddressDetails()).thenReturn(mock(AddressDetails.class));
        when(geoAddress.getAddressDetails().getCountry()).thenReturn(country);
        when(country.getCountryNameCode()).thenReturn(Collections.singletonList(countryCode));
        when(country.getAdministrativeArea()).thenReturn(adminArea);
        when(adminArea.getAdministrativeAreaName()).thenReturn(Collections.singletonList(adminAreaName));
        when(adminArea.getSubAdministrativeArea()).thenReturn(subAdminArea);
        when(subAdminArea.getSubAdministrativeAreaName()).thenReturn(Collections.singletonList(subAdminAreaName));
        when(adminArea.getLocality()).thenReturn(locality);
        when(locality.getLocalityName()).thenReturn(Collections.singletonList(localityName));
        when(locality.getPostalCode()).thenReturn(postalCode);
        when(postalCode.getPostalCodeNumber()).thenReturn(Collections.singletonList(postalCodeNumber));
        when(locality.getThoroughfare()).thenReturn(thoroughfare);
        when(thoroughfare.getThoroughfareName()).thenReturn(Collections.singletonList(thoroughfareName));
    }

    @Test
    public void testExecute() {
        GeoUsAddress geoUsAddress = addressToUsAddressFunctor.execute(geoAddress);

        verify(geoAddress).getAddressDetails();
        verify(geoAddress.getAddressDetails()).getCountry();
        verify(country).getCountryNameCode();
        verify(country).getAdministrativeArea();
        verify(adminArea).getAdministrativeAreaName();
        verify(adminArea).getSubAdministrativeArea();
        verify(subAdminArea).getSubAdministrativeAreaName();
        verify(adminArea).getLocality();
        verify(locality).getLocalityName();
        verify(locality).getPostalCode();
        verify(postalCode).getPostalCodeNumber();
        verify(locality).getThoroughfare();
        verify(thoroughfare).getThoroughfareName();

        assertNull(geoUsAddress.getCountry());
        assertNull(geoUsAddress.getState());
        assertNull(geoUsAddress.getCounty());
        assertNull(geoUsAddress.getCity());
        assertNull(geoUsAddress.getPostalCode());
        assertNull(geoUsAddress.getAddressLine1());
        assertNull(geoUsAddress.getAddressLine2());
        assertNull(geoUsAddress.getCoordinate());
        assertEquals(0, geoUsAddress.getAccuracy());
    }
}